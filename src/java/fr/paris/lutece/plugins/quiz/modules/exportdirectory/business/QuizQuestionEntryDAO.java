/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.quiz.modules.exportdirectory.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * DAO for quiz questions and directory entries association
 */
public class QuizQuestionEntryDAO implements IQuizQuestionEntryDAO
{
    private static final String SQL_QUERY_SELECT_ASSOCIATION = " SELECT id_entry FROM quiz_exportdirectory_associations WHERE id_question = ? ";
    private static final String SQL_QUERY_INSERT_ASSOCIATION = " INSERT INTO quiz_exportdirectory_associations ( id_quiz, id_question, id_entry ) VALUES (?,?,?)";
    private static final String SQL_QUERY_INSERT_SCORE = " INSERT INTO quiz_exportdirectory_associations ( id_quiz, id_entry ) VALUES (?,?)";
    private static final String SQL_QUERY_REMOVE_ASSOCIATION = " DELETE FROM quiz_exportdirectory_associations WHERE id_question = ? ";
    private static final String SQL_QUERY_REMOVE_SCORE = " DELETE FROM quiz_exportdirectory_associations WHERE id_quiz = ? AND id_question IS NULL";
    private static final String SQL_QUERY_SELECT_ALL_ASSOCIATIONS_BY_QUIZ = " SELECT assoc.id_question, assoc.id_entry FROM quiz_exportdirectory_associations assoc INNER JOIN quiz_question question ON assoc.id_question = question.id_question WHERE question.id_quiz = ? ";
    private static final String SQL_QUERY_SELECT_SCORE_BY_QUIZ = " SELECT assoc.id_quiz, assoc.id_entry FROM quiz_exportdirectory_associations assoc WHERE assoc.id_quiz = ? AND assoc.id_question IS NULL";
    private static final String SQL_QUERY_IS_ENTRY_ASSOCIATED = " SELECT id_entry FROM quiz_exportdirectory_associations WHERE id_entry = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQuestionAssociation( int nIdQuestion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ASSOCIATION, plugin );
        daoUtil.setInt( 1, nIdQuestion );
        daoUtil.executeQuery( );
        int nIdEntry = 0;
        if ( daoUtil.next( ) )
        {
            nIdEntry = daoUtil.getInt( 1 );
        }
        daoUtil.free( );
        return nIdEntry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAssociateQuestionAndEntry( int nIdQuiz, int nIdQuestion, int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ASSOCIATION, plugin );
        daoUtil.setInt( 1, nIdQuiz );
        daoUtil.setInt( 2, nIdQuestion );
        daoUtil.setInt( 3, nIdEntry );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    @Override
    public void doAssociateScoreAndEntry( int nIdQuiz, int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_SCORE, plugin );
        daoUtil.setInt( 1, nIdQuiz );
        daoUtil.setInt( 2, nIdEntry );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveAssociation( int nIdQuestion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_ASSOCIATION, plugin );
        daoUtil.setInt( 1, nIdQuestion );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveScore( int ndQuiz, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_SCORE, plugin );
        daoUtil.setInt( 1, ndQuiz );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Integer> getQuestionAssociations( int nIdQuiz, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_ASSOCIATIONS_BY_QUIZ, plugin );
        daoUtil.setInt( 1, nIdQuiz );
        daoUtil.executeQuery( );
        Map<Integer, Integer> mapQuestionsEntries = new HashMap<Integer, Integer>( );
        while ( daoUtil.next( ) )
        {
            mapQuestionsEntries.put( daoUtil.getInt( 1 ), daoUtil.getInt( 2 ) );
        }
        daoUtil.free( );
        return mapQuestionsEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Integer> getQuestionAssociationScore( int nIdQuiz, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_SCORE_BY_QUIZ, plugin );
        daoUtil.setInt( 1, nIdQuiz );
        daoUtil.executeQuery( );
        Map<Integer, Integer> mapQuestionsEntries = new HashMap<Integer, Integer>( );
        while ( daoUtil.next( ) )
        {
            mapQuestionsEntries.put( daoUtil.getInt( 1 ), daoUtil.getInt( 2 ) );
        }
        daoUtil.free( );
        return mapQuestionsEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryAssociated( int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_IS_ENTRY_ASSOCIATED, plugin );
        daoUtil.setInt( 1, nIdEntry );
        daoUtil.executeQuery( );
        boolean bResult;
        if ( daoUtil.next( ) )
        {
            bResult = true;
        }
        else
        {
            bResult = false;
        }
        daoUtil.free( );
        return bResult;
    }
}
