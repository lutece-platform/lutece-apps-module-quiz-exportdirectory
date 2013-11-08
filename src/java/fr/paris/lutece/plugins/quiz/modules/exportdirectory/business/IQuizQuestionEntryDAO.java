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

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Map;


/**
 * Interface of DAO for quiz questions and directory entries association
 */
public interface IQuizQuestionEntryDAO
{
    /**
     * Get the id of the entry associated with the question
     * @param nIdQuestion The id of the question
     * @param plugin The plugin
     * @return the id of the entry associated with the question, or 0 if the
     *         question is not associated with any entry
     */
    int getQuestionAssociation( int nIdQuestion, Plugin plugin );

    /**
     * Associates a question with a entry
     * @param nIdQuestion The id of the question
     * @param nIdEntry The id of the entry
     * @param plugin the plugin
     */
    void doAssociateQuestionAndEntry( int nIdQuestion, int nIdEntry, Plugin plugin );

    /**
     * Remove an association between a question and a entry
     * @param nIdQuestion The id of the question
     * @param plugin The plugin
     */
    void doRemoveAssociation( int nIdQuestion, Plugin plugin );

    /**
     * Get the association between questions of a quiz and directory entries
     * @param nIdQuiz The id of the quiz
     * @param plugin The plugin
     * @return A map which keys are id of questions of the quiz, and values are
     *         id of entries of the directory associated with the quiz
     */
    Map<Integer, Integer> getQuestionAssociations( int nIdQuiz, Plugin plugin );
}
