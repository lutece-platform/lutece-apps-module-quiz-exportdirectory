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

/**
 * Describe the mapping between a free HTML parameter and an entry of the
 * directory
 */
public class FreeHtmlParameter
{
    private int _nIdParameter;
    private int _nIdQuiz;
    private String _strParameterName;
    private int _nIdEntry;

    /**
     * Get the id of the parameter
     * @return The id of the parameter
     */
    public int getIdParameter( )
    {
        return _nIdParameter;
    }

    /**
     * Set the id of the parameter
     * @param nIdParameter The id of the parameter
     */
    public void setIdParameter( int nIdParameter )
    {
        this._nIdParameter = nIdParameter;
    }

    /**
     * Get the id of the quiz
     * @return The id of the quiz
     */
    public int getIdQuiz( )
    {
        return _nIdQuiz;
    }

    /**
     * Set the id of the quiz
     * @param nIdQuiz The id of the quiz
     */
    public void setIdQuiz( int nIdQuiz )
    {
        this._nIdQuiz = nIdQuiz;
    }

    /**
     * Get the name of the HTTP parameter
     * @return The name of the HTTP parameter
     */
    public String getParameterName( )
    {
        return _strParameterName;
    }

    /**
     * Set the name of the HTTP parameter
     * @param strParameterName The name of the HTTP parameter
     */
    public void setParameterName( String strParameterName )
    {
        this._strParameterName = strParameterName;
    }

    /**
     * Get the id of the entry associated with this parameter
     * @return The id of the entry associated with this parameter
     */
    public int getIdEntry( )
    {
        return _nIdEntry;
    }

    /**
     * Set the id of the entry associated with this parameter
     * @param nIdEntry The id of the entry associated with this parameter
     */
    public void setIdEntry( int nIdEntry )
    {
        this._nIdEntry = nIdEntry;
    }

}
