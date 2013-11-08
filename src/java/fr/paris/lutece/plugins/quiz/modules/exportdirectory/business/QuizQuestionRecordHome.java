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
package fr.paris.lutece.plugins.quiz.modules.exportdirectory.business;

import fr.paris.lutece.plugins.quiz.modules.exportdirectory.service.QuizExportDirectoryPlugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Map;


/**
 * Home for Quiz questions and directory records association
 */
public class QuizQuestionRecordHome
{
    private static IQuizQuestionRecordDAO _dao = SpringContextService
            .getBean( "quiz-exportdirectory.quizQuestionRecordDAO" );

    /**
     * Get the id of the record associated with the question
     * @param nIdQuestion The id of the question
     * @return the id of the record associated with the question, or 0 if the
     *         question is not associated with any record
     */
    public static int getQuestionAssociation( int nIdQuestion )
    {
        return _dao.getQuestionAssociation( nIdQuestion, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Associates a question with a record
     * @param nIdQuestion The id of the question
     * @param nIdRecord The id of the record
     */
    public static void doAssociateQuestionAndRecord( int nIdQuestion, int nIdRecord )
    {
        _dao.doAssociateQuestionAndRecord( nIdQuestion, nIdRecord, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Remove an association between a question and a record
     * @param nIdQuestion The id of the question
     */
    public static void doRemoveAssociation( int nIdQuestion )
    {
        _dao.doRemoveAssociation( nIdQuestion, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Get the association between questions of a quiz and directory records
     * @param nIdQuiz The id of the quiz
     * @return A map which keys are id of questions of the quiz, and values are
     *         id of records of the directory associated with the quiz
     */
    public static Map<Integer, Integer> getQuestionAssociations( int nIdQuiz )
    {
        return _dao.getQuestionAssociations( nIdQuiz, QuizExportDirectoryPlugin.getPlugin( ) );
    }
}
