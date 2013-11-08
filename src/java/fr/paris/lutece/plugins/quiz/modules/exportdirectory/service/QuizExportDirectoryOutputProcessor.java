/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.quiz.modules.exportdirectory.service;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.quiz.business.QuizQuestion;
import fr.paris.lutece.plugins.quiz.business.QuizQuestionHome;
import fr.paris.lutece.plugins.quiz.modules.exportdirectory.business.FreeHtmlParameter;
import fr.paris.lutece.plugins.quiz.modules.exportdirectory.business.FreeHtmlParameterHome;
import fr.paris.lutece.plugins.quiz.modules.exportdirectory.business.QuizQuestionEntryHome;
import fr.paris.lutece.plugins.quiz.service.QuizService;
import fr.paris.lutece.plugins.quiz.service.outputprocessor.IQuizOutputProcessor;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * Output processor that export answers of a quiz into the plugin directory
 */
public class QuizExportDirectoryOutputProcessor implements IQuizOutputProcessor
{
    public static final String DATASTORE_KEY_QUIZ_EXPORT_DIRECTORY = "quiz-exportdirectory.export.idDirectory.";

    private static final String PROCESSOR_ID = "quiz-exportdirectory.exportDirectoryProcessor";

    private static final String MESSAGE_PROCESSOR_DESCRIPTION = "module.quiz.exportdirectory.exportDirectoryProcessor.title";
    private static final String MESSAGE_ERROR_ENTRY_ALREADY_USED = "module.quiz.exportdirectory.exportDirectoryProcessor.errorEntryAlreadyUsed";

    private static final String TEMPLATE_CONFIG = "admin/plugins/quiz/modules/exportdirectory/exportdirectory_processor_config.html";

    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_ENTRY_ID = "entry_id";
    private static final String PARAMETER_FREE_HTML_PARAMETER_NAME = "freeHtmlParameterName";
    private static final String PARAMETER_ID_DIRECTORY = "idDirectory";
    private static final String PARAMETER_HAS_DIRECTORY = "hasDirectory";

    private static final String MARK_ID_ENTRY_FOR_QUESTIONS = "idEntryForQuestions";
    private static final String MARK_ID_ENTRY_FOR_FREE_PARAMETERS = "idEntryForParameters";
    private static final String MARK_LIST_DIRECTORIES = "list_directories";
    private static final String MARK_LIST_PARAMETERS = "list_parameters";
    private static final String MARK_LIST_ENTRIES = "list_entries";
    private static final String MARK_INPUT_PREFIXE = "input_prefix";
    private static final String MARK_LIST_QUESTIONS = "list_questions";
    private static final String MARK_MAP_QUESTION_ENTRY = "map_question_entry";
    private static final String MARK_ERROR = "error";
    private static final String MARK_QUIZ_ID = "quiz_id";

    private static final String ACTION_DO_ADD_FREE_HTML = "addFreeHtml";
    private static final String ACTION_DO_REMOVE_FREE_HTML = "removeFreeHtml";

    private static final String PROPERTY_ENTRY_TYPE_TEXT_ID = "quiz-exportdirectory.entryTypeText.id";
    private static final String SESSION_ATTRIBUTE_ERROR = "quiz-exportdirectory.session.error";

    private static final int DEFAULT_ID_ENTRY_TYPE_TEXT = 6;

    private int _nIdEntryTypeText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doProcessOutputProcessor( Map<String, String[]> mapAnswers, int nIdQuiz )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Plugin pluginQuiz = PluginService.getPlugin( QuizService.PLUGIN_NAME );

        // We check that the quiz is associated to a directory
        String strIdDirectory = DatastoreService.getDataValue( getQuizDirectoryDatastoreKey( nIdQuiz ), null );
        if ( StringUtils.isNotEmpty( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
        {
            Collection<QuizQuestion> collectionQuestion = QuizQuestionHome.findAll( nIdQuiz, pluginQuiz );
            List<FreeHtmlParameter> listFreeParameters = FreeHtmlParameterHome.getFreeHtmlParameterList( nIdQuiz );

            Map<Integer, Integer> mapQuestionEntry = QuizQuestionEntryHome.getQuestionAssociations( nIdQuiz );
            List<Integer> listEntriesAnswered = new ArrayList<Integer>( );
            // We create the directory record
            int nIdDirectory = Integer.parseInt( strIdDirectory );
            Record record = new Record( );
            Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, pluginDirectory );
            record.setDirectory( directory );
            record.setDateCreation( new Timestamp( new Date( ).getTime( ) ) );
            record.setEnabled( directory.isRecordActivated( ) );
            record.setListRecordField( new ArrayList<RecordField>( ) );
            RecordHome.create( record, pluginDirectory );

            // We create every record field for questions associated with an entry of the directory
            for ( QuizQuestion question : collectionQuestion )
            {
                Integer nIdEntry = mapQuestionEntry.get( question.getIdQuestion( ) );
                if ( nIdEntry != null && nIdEntry > 0 && !listEntriesAnswered.contains( nIdEntry ) )
                {
                    listEntriesAnswered.add( nIdEntry );
                    String strQuestionId = String.valueOf( question.getIdQuestion( ) );
                    String[] values = mapAnswers.get( strQuestionId );
                    doCreateDirectoryRecordField( nIdEntry, record, values, pluginDirectory );
                }
            }

            // We create every record field for free parameters associated with an entry of the directory
            for ( FreeHtmlParameter parameter : listFreeParameters )
            {
                if ( parameter.getIdEntry( ) > 0 && !listEntriesAnswered.contains( parameter.getIdEntry( ) ) )
                {
                    listEntriesAnswered.add( parameter.getIdEntry( ) );
                    String[] values = mapAnswers.get( parameter.getParameterName( ) );
                    doCreateDirectoryRecordField( parameter.getIdEntry( ), record, values, pluginDirectory );
                }
            }
        }
    }

    /**
     * Do create a directory record field for an answer of the quiz
     * @param nIdEntry The id of the entry associated with the record field
     * @param record The record associated with the answered question (or the
     *            free parameter)
     * @param strArrayValue The array of answers of the user
     * @param pluginDirectory the directory plugin
     */
    private void doCreateDirectoryRecordField( int nIdEntry, Record record, String[] strArrayValue,
            Plugin pluginDirectory )
    {
        if ( strArrayValue != null && strArrayValue.length > 0 )
        {
            IEntry entryDirectory = EntryHome.findByPrimaryKey( nIdEntry, pluginDirectory );

            if ( entryDirectory != null )
            {
                RecordField recordField = new RecordField( );
                recordField.setEntry( entryDirectory );
                recordField.setValue( strArrayValue[0] );
                recordField.setRecord( record );
                RecordFieldHome.create( recordField, pluginDirectory );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProcessorConfigurationHtml( HttpServletRequest request, int nIdQuiz )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        String strIdDirectory = DatastoreService.getDataValue( getQuizDirectoryDatastoreKey( nIdQuiz ), null );
        // If the quiz is already associated with a directory
        if ( StringUtils.isNotEmpty( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
        {
            int nIdDirectory = Integer.parseInt( strIdDirectory );
            model.put( PARAMETER_ID_DIRECTORY, strIdDirectory );
            model.put( PARAMETER_HAS_DIRECTORY, Boolean.TRUE );

            EntryFilter filter = new EntryFilter( );
            filter.setIdDirectory( nIdDirectory );
            filter.setIdType( getIdEntryTypeText( ) );
            List<IEntry> listEntries = EntryHome.getEntryList( filter, pluginDirectory );

            ReferenceList refListEntries = new ReferenceList( );
            ReferenceItem refItem = new ReferenceItem( );
            refItem.setCode( StringUtils.EMPTY );
            refItem.setName( StringUtils.EMPTY );
            refListEntries.add( refItem );
            for ( IEntry entry : listEntries )
            {
                refItem = new ReferenceItem( );
                refItem.setCode( Integer.toString( entry.getIdEntry( ) ) );
                refItem.setName( entry.getTitle( ) );
                refListEntries.add( refItem );
            }

            model.put( MARK_LIST_ENTRIES, refListEntries );

            List<FreeHtmlParameter> listParameters = FreeHtmlParameterHome.getFreeHtmlParameterList( nIdQuiz );
            model.put( MARK_LIST_PARAMETERS, listParameters );

            String strPrefix = AppPropertiesService.getProperty( QuizService.PROPERTY_INPUT_PREFIX );
            model.put( MARK_INPUT_PREFIXE, strPrefix );

            Plugin pluginQuiz = PluginService.getPlugin( QuizService.PLUGIN_NAME );
            Collection<QuizQuestion> collectionQuestion = QuizQuestionHome.findAll( nIdQuiz, pluginQuiz );

            Map<Integer, Integer> mapQuestionEntry = QuizQuestionEntryHome.getQuestionAssociations( nIdQuiz );
            Map<String, String> mapQuestionsString = new HashMap<String, String>( mapQuestionEntry.size( ) );
            for ( QuizQuestion question : collectionQuestion )
            {
                if ( mapQuestionEntry.get( question.getIdQuestion( ) ) == null )
                {
                    mapQuestionsString.put( Integer.toString( question.getIdQuestion( ) ), StringUtils.EMPTY );
                }
            }

            for ( Entry<Integer, Integer> entry : mapQuestionEntry.entrySet( ) )
            {
                mapQuestionsString.put( entry.getKey( ).toString( ), entry.getValue( ).toString( ) );
            }

            model.put( MARK_LIST_QUESTIONS, collectionQuestion );
            model.put( MARK_MAP_QUESTION_ENTRY, mapQuestionsString );
        }
        else
        {
            model.put( PARAMETER_ID_DIRECTORY, StringUtils.EMPTY );
            model.put( PARAMETER_HAS_DIRECTORY, Boolean.FALSE );
        }
        //        List<FreeHtmlParameter> listFreeHtmlParameters = 

        String strError = (String) request.getSession( ).getAttribute( SESSION_ATTRIBUTE_ERROR );
        if ( StringUtils.isNotEmpty( strError ) )
        {
            model.put( MARK_ERROR, strError );
            request.getSession( ).removeAttribute( SESSION_ATTRIBUTE_ERROR );
        }

        ReferenceList refListDirectories = DirectoryHome.getDirectoryList( pluginDirectory );
        ReferenceList refListDirectoriesWithEmptyElement = new ReferenceList( refListDirectories.size( ) + 1 );
        refListDirectoriesWithEmptyElement.addItem( 0, StringUtils.EMPTY );
        refListDirectoriesWithEmptyElement.addAll( refListDirectories );
        model.put( MARK_LIST_DIRECTORIES, refListDirectoriesWithEmptyElement );
        model.put( MARK_QUIZ_ID, nIdQuiz );

        Locale locale = AdminUserService.getLocale( request );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model );
        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdateConfiguration( HttpServletRequest request, int nIdQuiz )
    {
        String strAction = request.getParameter( PARAMETER_ACTION );
        if ( StringUtils.equals( strAction, ACTION_DO_ADD_FREE_HTML ) )
        {
            doAddFreeHtmlParameter( request, nIdQuiz );
            return;
        }
        if ( StringUtils.isNotEmpty( request.getParameter( ACTION_DO_REMOVE_FREE_HTML ) ) )
        {
            doRemoveFreeHtmlParameter( request );
            return;
        }
        updateQuizDirectoryMapping( request, nIdQuiz );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyProcessorEnabling( int nIdQuiz )
    {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyProcessorDisabling( int nIdQuiz )
    {
        DatastoreService.removeData( getQuizDirectoryDatastoreKey( nIdQuiz ) );
        List<FreeHtmlParameter> listParameters = FreeHtmlParameterHome.getFreeHtmlParameterList( nIdQuiz );
        for ( FreeHtmlParameter parameter : listParameters )
        {
            FreeHtmlParameterHome.removeFreeHtmlParameter( parameter.getIdParameter( ) );
        }

        Plugin pluginQuiz = PluginService.getPlugin( QuizService.PLUGIN_NAME );
        Collection<QuizQuestion> listQuestions = QuizQuestionHome.findAll( nIdQuiz, pluginQuiz );
        for ( QuizQuestion question : listQuestions )
        {
            QuizQuestionEntryHome.doRemoveAssociation( question.getIdQuestion( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProcessorId( )
    {
        return PROCESSOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_PROCESSOR_DESCRIPTION, locale );
    }

    /**
     * Do perform the add a free HTML parameter action
     * @param request The request
     * @param nIdQuiz the id of the quiz
     */
    private void doAddFreeHtmlParameter( HttpServletRequest request, int nIdQuiz )
    {
        String strParameterName = request.getParameter( PARAMETER_FREE_HTML_PARAMETER_NAME );
        String strIdEntry = request.getParameter( PARAMETER_ENTRY_ID );
        int nIdEntry;
        if ( StringUtils.isEmpty( strIdEntry ) || !StringUtils.isNumeric( strIdEntry ) )
        {
            nIdEntry = 0;
        }
        else
        {
            nIdEntry = Integer.parseInt( strIdEntry );
        }

        if ( StringUtils.isNotEmpty( strParameterName ) )
        {
            strParameterName = AppPropertiesService.getProperty( QuizService.PROPERTY_INPUT_PREFIX ) + strParameterName;
            FreeHtmlParameter parameter = new FreeHtmlParameter( );
            parameter.setIdQuiz( nIdQuiz );
            parameter.setParameterName( strParameterName );
            parameter.setIdEntry( nIdEntry );
            FreeHtmlParameterHome.addFreeHtmlParameter( parameter );
        }
    }

    /**
     * Do perform the remove a free html parameter action
     * @param request the request
     */
    private void doRemoveFreeHtmlParameter( HttpServletRequest request )
    {
        int nIdParameter = Integer.parseInt( request.getParameter( ACTION_DO_REMOVE_FREE_HTML ) );
        FreeHtmlParameterHome.removeFreeHtmlParameter( nIdParameter );
    }

    /**
     * Update the mapping between a quiz and a directory
     * @param request The request
     * @param nIdQuiz The id of the quiz
     */
    private void updateQuizDirectoryMapping( HttpServletRequest request, int nIdQuiz )
    {
        String strIdNewDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        if ( StringUtils.isEmpty( strIdNewDirectory ) || !StringUtils.isNumeric( strIdNewDirectory ) )
        {
            return;
        }
        int nIdNewDirectory = Integer.parseInt( strIdNewDirectory );

        // We check if the quiz was already associated with a directory
        String strDatastoreKey = getQuizDirectoryDatastoreKey( nIdQuiz );
        String strIdDirectory = DatastoreService.getDataValue( strDatastoreKey, null );
        if ( nIdNewDirectory == 0 )
        {
            notifyProcessorDisabling( nIdQuiz );
            return;
        }
        if ( StringUtils.isNotEmpty( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
        {
            int nIdDirectory = Integer.parseInt( strIdDirectory );
            // If the directory has changed
            if ( nIdNewDirectory != nIdDirectory )
            {
                // We remove the current configuration of the quiz
                notifyProcessorDisabling( nIdQuiz );
                // We save the new association
                DatastoreService.setDataValue( strDatastoreKey, strIdNewDirectory );
            }
            else
            {
                Plugin pluginQuiz = PluginService.getPlugin( QuizService.PLUGIN_NAME );

                // We update the association between questions and records
                Collection<QuizQuestion> listQuestions = QuizQuestionHome.findAll( nIdQuiz, pluginQuiz );
                List<FreeHtmlParameter> listParameters = FreeHtmlParameterHome.getFreeHtmlParameterList( nIdQuiz );
                List<Integer> listUsedIdEntry = new ArrayList<Integer>( listQuestions.size( ) + listParameters.size( ) );
                Map<Integer, Integer> mapQuestionRecord = new HashMap<Integer, Integer>( listQuestions.size( ) );
                for ( QuizQuestion question : listQuestions )
                {
                    String strIdEntry = request.getParameter( MARK_ID_ENTRY_FOR_QUESTIONS + question.getIdQuestion( ) );
                    if ( StringUtils.isNotEmpty( strIdEntry ) && StringUtils.isNumeric( strIdEntry ) )
                    {
                        int nIdEntry = Integer.parseInt( strIdEntry );
                        if ( listUsedIdEntry.contains( nIdEntry ) )
                        {
                            request.getSession( ).setAttribute( SESSION_ATTRIBUTE_ERROR,
                                    MESSAGE_ERROR_ENTRY_ALREADY_USED );
                            return;
                        }
                        listUsedIdEntry.add( nIdEntry );
                        mapQuestionRecord.put( question.getIdQuestion( ), nIdEntry );
                    }
                }

                for ( FreeHtmlParameter parameter : listParameters )
                {
                    String strIdEntry = request.getParameter( MARK_ID_ENTRY_FOR_FREE_PARAMETERS
                            + parameter.getIdParameter( ) );
                    if ( StringUtils.isNotEmpty( strIdEntry ) && StringUtils.isNumeric( strIdEntry ) )
                    {
                        int nIdEntry = Integer.parseInt( strIdEntry );
                        if ( listUsedIdEntry.contains( nIdEntry ) )
                        {
                            request.getSession( ).setAttribute( SESSION_ATTRIBUTE_ERROR,
                                    MESSAGE_ERROR_ENTRY_ALREADY_USED );
                            return;
                        }
                        listUsedIdEntry.add( nIdEntry );
                        parameter.setIdEntry( nIdEntry );
                    }
                    else
                    {
                        parameter.setIdEntry( 0 );
                    }
                }

                // If there is no error, we save the mapping
                // We first remove every association between questions and entries
                for ( QuizQuestion question : listQuestions )
                {
                    QuizQuestionEntryHome.doRemoveAssociation( question.getIdQuestion( ) );
                }
                // We now save associations between questions and entries
                for ( Entry<Integer, Integer> entry : mapQuestionRecord.entrySet( ) )
                {
                    QuizQuestionEntryHome.doAssociateQuestionAndEntry( entry.getKey( ), entry.getValue( ) );
                }
                // Finally we update free HTML parameters
                for ( FreeHtmlParameter parameter : listParameters )
                {
                    FreeHtmlParameterHome.modifyFreeHtmlParameter( parameter );
                }
            }
        }
        else
        {
            // We associate the directory to the quiz
            DatastoreService.setDataValue( strDatastoreKey, strIdNewDirectory );
        }
    }

    /**
     * Get the datastore key to associate a quiz to a directory
     * @param nIdQuiz The id of the quiz
     * @return The datastore key
     */
    private String getQuizDirectoryDatastoreKey( int nIdQuiz )
    {
        return DATASTORE_KEY_QUIZ_EXPORT_DIRECTORY + nIdQuiz;
    }

    /**
     * Get the id of entries of type text
     * @return The id of entries of type text
     */
    private int getIdEntryTypeText( )
    {
        if ( _nIdEntryTypeText == 0 )
        {
            _nIdEntryTypeText = AppPropertiesService.getPropertyInt( PROPERTY_ENTRY_TYPE_TEXT_ID,
                    DEFAULT_ID_ENTRY_TYPE_TEXT );
        }
        return _nIdEntryTypeText;
    }

}
