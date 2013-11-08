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

import java.util.List;


/**
 * Home for free HTML parameters
 */
public final class FreeHtmlParameterHome
{
    private static IFreeHtmlParameterDAO _dao = SpringContextService
            .getBean( "quiz-exportdirectory.freeHtmlParameterDAO" );

    /**
     * Private constructor
     */
    private FreeHtmlParameterHome( )
    {
        // Do nothing
    }

    /**
     * Add a parameter
     * @param parameter The parameter to add
     */
    public static void addFreeHtmlParameter( FreeHtmlParameter parameter )
    {
        _dao.addFreeHtmlParameter( parameter, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Find a parameter from its primary key
     * @param nIdParameter The id of the parameter
     * @return The parameter, or null if no parameter has the given id
     */
    public static FreeHtmlParameter findByPrimaryKey( int nIdParameter )
    {
        return _dao.findByPrimaryKey( nIdParameter, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Add a parameter
     * @param parameter The parameter to modify
     */
    public static void modifyFreeHtmlParameter( FreeHtmlParameter parameter )
    {
        _dao.modifyFreeHtmlParameter( parameter, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Remove a parameter associated with a quiz
     * @param nIdParameter The id of the parameter to remove
     */
    public static void removeFreeHtmlParameter( int nIdParameter )
    {
        _dao.removeFreeHtmlParameter( nIdParameter, QuizExportDirectoryPlugin.getPlugin( ) );
    }

    /**
     * Get the list of parameters associated with a given quiz
     * @param nIdQuiz the quiz
     * @return the list of parameters. The list may be empty but is never null.
     */
    public static List<FreeHtmlParameter> getFreeHtmlParameterList( int nIdQuiz )
    {
        return _dao.getFreeHtmlParameterList( nIdQuiz, QuizExportDirectoryPlugin.getPlugin( ) );
    }
}
