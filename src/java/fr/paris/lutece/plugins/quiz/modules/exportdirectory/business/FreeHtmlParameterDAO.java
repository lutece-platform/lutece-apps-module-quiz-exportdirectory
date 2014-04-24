/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.ArrayList;
import java.util.List;


/**
 * DAO for free html parameters
 */
public class FreeHtmlParameterDAO implements IFreeHtmlParameterDAO
{
    private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT MAX(id_parameter) FROM quiz_exportdirectory_parameters";
    private static final String SQL_QUERY_ADD_FREE_HTML_PARAMETER = " INSERT INTO quiz_exportdirectory_parameters( id_parameter, id_quiz, parameter_name, id_entry ) VALUES (?,?,?,?) ";
    private static final String SQL_QUERY_REMOVE_FREE_HTML_PARAMETER = " DELETE FROM quiz_exportdirectory_parameters WHERE id_parameter = ? ";
    private static final String SQL_QUERY_UPDATE_FREE_HTML_PARAMETER = " UPDATE quiz_exportdirectory_parameters SET id_quiz = ?, parameter_name = ?, id_entry = ? WHERE id_parameter = ? ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_parameter, id_quiz, parameter_name, id_entry FROM quiz_exportdirectory_parameters WHERE id_parameter = ? ";
    private static final String SQL_QUERY_FIND_FREE_HTML_PARAMETER_LIST = " SELECT id_parameter, id_quiz, parameter_name, id_entry FROM quiz_exportdirectory_parameters WHERE id_quiz = ? ";
    private static final String SQL_QUERY_IS_ENTRY_ASSOCIATED = "SELECT id_entry FROM quiz_exportdirectory_parameters WHERE id_entry = ?";

    /**
     * Get a new primary key
     * @param plugin the plugin
     * @return The new primary key
     */
    private int getNewPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY, plugin );
        daoUtil.executeQuery( );
        int nId = 0;
        if ( daoUtil.next( ) )
        {
            nId = daoUtil.getInt( 1 ) + 1;
        }
        daoUtil.free( );
        return nId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addFreeHtmlParameter( FreeHtmlParameter parameter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ADD_FREE_HTML_PARAMETER, plugin );
        parameter.setIdParameter( getNewPrimaryKey( plugin ) );
        daoUtil.setInt( 1, parameter.getIdParameter( ) );
        daoUtil.setInt( 2, parameter.getIdQuiz( ) );
        daoUtil.setString( 3, parameter.getParameterName( ) );
        daoUtil.setInt( 4, parameter.getIdEntry( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FreeHtmlParameter findByPrimaryKey( int nIdParameter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nIdParameter );
        daoUtil.executeQuery( );
        FreeHtmlParameter parameter = null;
        if ( daoUtil.next( ) )
        {
            parameter = new FreeHtmlParameter( );
            parameter.setIdParameter( daoUtil.getInt( 1 ) );
            parameter.setIdQuiz( daoUtil.getInt( 2 ) );
            parameter.setParameterName( daoUtil.getString( 3 ) );
            parameter.setIdEntry( daoUtil.getInt( 4 ) );
        }

        daoUtil.free( );

        return parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyFreeHtmlParameter( FreeHtmlParameter parameter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_FREE_HTML_PARAMETER, plugin );
        daoUtil.setInt( 1, parameter.getIdQuiz( ) );
        daoUtil.setString( 2, parameter.getParameterName( ) );
        daoUtil.setInt( 3, parameter.getIdEntry( ) );
        daoUtil.setInt( 4, parameter.getIdParameter( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFreeHtmlParameter( int nIdParameter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_FREE_HTML_PARAMETER, plugin );
        daoUtil.setInt( 1, nIdParameter );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FreeHtmlParameter> getFreeHtmlParameterList( int nIdQuiz, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_FREE_HTML_PARAMETER_LIST, plugin );
        daoUtil.setInt( 1, nIdQuiz );
        daoUtil.executeQuery( );
        List<FreeHtmlParameter> listParams = new ArrayList<FreeHtmlParameter>( );
        while ( daoUtil.next( ) )
        {
            FreeHtmlParameter parameter = new FreeHtmlParameter( );
            parameter.setIdParameter( daoUtil.getInt( 1 ) );
            parameter.setIdQuiz( daoUtil.getInt( 2 ) );
            parameter.setParameterName( daoUtil.getString( 3 ) );
            parameter.setIdEntry( daoUtil.getInt( 4 ) );
            listParams.add( parameter );
        }

        daoUtil.free( );

        return listParams;
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
