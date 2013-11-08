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

import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.quiz.business.QuizQuestion;
import fr.paris.lutece.plugins.quiz.business.QuizQuestionHome;
import fr.paris.lutece.plugins.quiz.modules.exportdirectory.business.FreeHtmlParameter;
import fr.paris.lutece.plugins.quiz.modules.exportdirectory.business.FreeHtmlParameterHome;
import fr.paris.lutece.plugins.quiz.service.QuizService;
import fr.paris.lutece.plugins.quiz.service.outputprocessor.IQuizOutputProcessor;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * Output processor that export answers of a quiz into the plugin directory
 */
public class QuizExportDirectoryOutputProcessor implements IQuizOutputProcessor
{
    private static final String PROCESSOR_ID = "quiz-exportdirectory.exportDirectoryProcessor";

    private static final String DATASTORE_KEY_QUIZ_EXPORT_DIRECTORY = "quiz-exportdirectory.export.idDirectory.";

    private static final String MESSAGE_PROCESSOR_DESCRIPTION = "module.quiz.exportdirectory.exportDirectoryProcessor.title";

    private static final String TEMPLATE_CONFIG = "admin/plugins/quiz/modules/exportdirectory/exportdirectory_processor_config.html";

    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_ENTRY_ID = "entry_id";
    private static final String PARAMETER_FREE_HTML_PARAMETER_NAME = "freeHtmlParameterName";
    private static final String PARAMETER_ID_DIRECTORY = "idDirectory";
    private static final String PARAMETER_HAS_DIRECTORY = "hasDirectory";

    private static final String MARK_ID_RECORD = "idRecord";
    private static final String MARK_LIST_DIRECTORIES = "list_directories";
    private static final String MARK_LIST_PARAMETERS = "list_parameters";
    private static final String MARK_LIST_ENTRIES = "list_entries";

    private static final String ACTION_DO_ADD_FREE_HTML = "addFreeHtml";
    private static final String ACTION_DO_REMOVE_FREE_HTML = "removeFreeHtml";
    private static final String ACTION_DO_MODIFY_DIRECTORY = "doModifyDirectory";

    private static final int DEFAULT_ID_ENTRY_TYPE_TEXT = 6;

    private QuizService _quizService;

    private static final String PROPERTY_ENTRY_TYPE_TEXT_ID = "quiz-exportdirectory.entryTypeText.id";

    private int _nIdEntryTypeText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doProcessOutputProcessor( Map<String, String[]> mapAnswers, int nIdQuiz )
    {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProcessorConfigurationHtml( HttpServletRequest request, int nIdQuiz )
    {
        Map<String, Object> model = new HashMap<String, Object>( );

        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Plugin pluginQuiz = PluginService.getPlugin( QuizService.PLUGIN_NAME );

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
            model.put( MARK_LIST_ENTRIES, listEntries );

            List<FreeHtmlParameter> listParameters = FreeHtmlParameterHome.getFreeHtmlParameterList( nIdQuiz );
            model.put( MARK_LIST_PARAMETERS, listParameters );
        }
        else
        {
            model.put( PARAMETER_ID_DIRECTORY, StringUtils.EMPTY );
            model.put( PARAMETER_HAS_DIRECTORY, Boolean.FALSE );
        }
        //        List<FreeHtmlParameter> listFreeHtmlParameters = 

        ReferenceList refListDirectories = DirectoryHome.getDirectoryList( pluginDirectory );
        model.put( MARK_LIST_DIRECTORIES, refListDirectories );

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
        }
        else if ( StringUtils.isNotEmpty( request.getParameter( ACTION_DO_REMOVE_FREE_HTML ) ) )
        {
            doRemoveFreeHtmlParameter( request );
        }
        else if ( StringUtils.equals( strAction, ACTION_DO_MODIFY_DIRECTORY ) )
        {
            String strIdNewDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
            if ( StringUtils.isNotEmpty( strIdNewDirectory ) && StringUtils.isNumeric( strIdNewDirectory ) )
            {
                int nIdNewDirectory = Integer.parseInt( strIdNewDirectory );
                // We check if the quiz was already associated with a directory
                String strDatastoreKey = getQuizDirectoryDatastoreKey( nIdQuiz );
                String strIdDirectory = DatastoreService.getDataValue( strDatastoreKey, null );
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
                        List<Integer> listUsedIdRecord = new ArrayList<Integer>( listQuestions.size( ) );
                        for ( QuizQuestion question : listQuestions )
                        {
                            String strIdRecord = request.getParameter( MARK_ID_RECORD + question.getIdQuestion( ) );
                            // TODO implement me !
                        }
                    }
                }
                else
                {
                    // We associate the directory to the quiz
                    DatastoreService.setDataValue( strDatastoreKey, strIdNewDirectory );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyProcessorEnabling( int nIdQuiz )
    {
        // TODO Auto-generated method stub

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

        // TODO Remove associations between quiz questions and directory records

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
        int nIdEntry = Integer.parseInt( request.getParameter( PARAMETER_ENTRY_ID ) );

        if ( StringUtils.isNotEmpty( strParameterName ) )
        {
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
     * Get the datastore key to associate a quiz to a directory
     * @param nIdQuiz The id of the quiz
     * @return The datastore key
     */
    private String getQuizDirectoryDatastoreKey( int nIdQuiz )
    {
        return DATASTORE_KEY_QUIZ_EXPORT_DIRECTORY + nIdQuiz;
    }

    /**
     * Get the quiz service
     * @return The quiz service
     */
    private QuizService getQuizService( )
    {
        if ( _quizService == null )
        {
            _quizService = SpringContextService.getBean( QuizService.BEAN_QUIZ_SERVICE );
        }
        return _quizService;
    }

    /**
     * Get the id of entries of type text
     * @return The id of entries opf type text
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
