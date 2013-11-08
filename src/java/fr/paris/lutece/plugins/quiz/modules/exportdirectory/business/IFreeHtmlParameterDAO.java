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

import java.util.List;


/**
 * Interface for free HTML parameter DAO
 */
public interface IFreeHtmlParameterDAO
{
    /**
     * Add a parameter
     * @param parameter The parameter to add
     * @param plugin The plugin
     */
    void addFreeHtmlParameter( FreeHtmlParameter parameter, Plugin plugin );

    /**
     * Find a parameter from its primary key
     * @param nIdParameter The id of the parameter
     * @param plugin The plugin
     * @return The parameter, or null if no parameter has the given id
     */
    FreeHtmlParameter findByPrimaryKey( int nIdParameter, Plugin plugin );

    /**
     * Add a parameter
     * @param parameter The parameter to modify
     * @param plugin The plugin
     */
    void modifyFreeHtmlParameter( FreeHtmlParameter parameter, Plugin plugin );

    /**
     * Remove a parameter associated with a quiz
     * @param nIdParameter The id of the parameter to remove
     * @param plugin The plugin
     */
    void removeFreeHtmlParameter( int nIdParameter, Plugin plugin );

    /**
     * Get the list of parameters associated with a given quiz
     * @param nIdQuiz the quiz
     * @param plugin The plugin
     * @return the list of parameters. The list may be empty but is never null.
     */
    List<FreeHtmlParameter> getFreeHtmlParameterList( int nIdQuiz, Plugin plugin );
}
