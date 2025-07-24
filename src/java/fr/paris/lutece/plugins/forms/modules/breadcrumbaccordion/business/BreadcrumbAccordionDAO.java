/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web.BreadcrumbAccordion;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class represents a Data Access Object for a SQL database for {@code BreadcrumbAccordion} objects
 *
 */
@ApplicationScoped
public class BreadcrumbAccordionDAO implements IBreadcrumbAccordionDAO
{
    private static final String SQL_QUERY_SELECT = "SELECT id_form, id_step, position FROM forms_breadcrumbaccordion_config_item";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_form, title FROM forms_form WHERE breadcrumb_name = '" + BreadcrumbAccordion.BEAN_NAME + "'";
    private static final String SQL_QUERY_INSERT = "INSERT INTO forms_breadcrumbaccordion_config_item (id_form, id_step, position) VALUES (?,?,?) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM forms_breadcrumbaccordion_config_item";
    private static final String SQL_QUERY_ID_FORM = " WHERE id_form=?";
    private static Plugin _plugin = PluginService.getPlugin( "forms-breadcrumbaccordion" );

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadcrumbAccordionConfig selectByIdForm( int nIdForm )
    {
    	BreadcrumbAccordionConfig breadcrumbAccordionConfig = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_QUERY_ID_FORM, _plugin ) )
        {
            int nPos = 0;
            daoUtil.setInt( ++nPos, nIdForm );

            daoUtil.executeQuery( );

            List<BreadcrumbAccordionConfigItem> listBreadcrumbAccordionConfigItem = new ArrayList<>( );

            while ( daoUtil.next( ) )
            {
                listBreadcrumbAccordionConfigItem.add( dataToObject( daoUtil ) );
            }

            breadcrumbAccordionConfig = new BreadcrumbAccordionConfig( nIdForm, listBreadcrumbAccordionConfigItem );
        }

        return breadcrumbAccordionConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( BreadcrumbAccordionConfig breadcrumbAccordionConfig )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, _plugin ) )
        {
            deleteByIdForm( breadcrumbAccordionConfig.getIdForm( ) );

            for ( BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem : breadcrumbAccordionConfig.getItems( ) )
            {
                int nPos = 0;
                daoUtil.setInt( ++nPos, breadcrumbAccordionConfig.getIdForm( ) );
                daoUtil.setInt( ++nPos, breadcrumbAccordionConfigItem.getIdStep( ) );
                daoUtil.setInt( ++nPos, breadcrumbAccordionConfigItem.getPosition( ) );

                daoUtil.executeUpdate( );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BreadcrumbAccordionConfig> selectBreadcrumbAccordionConfigList( )
    {
        List<BreadcrumbAccordionConfig> listBreadcrumbAccordionConfig = new ArrayList<>( );
	
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, _plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nPos = 0;
                int nIdForm = daoUtil.getInt( ++nPos );
                BreadcrumbAccordionConfig breadcrumbAccordionConfig = selectByIdForm( nIdForm );
                breadcrumbAccordionConfig.setIdForm( nIdForm );
                breadcrumbAccordionConfig.setFormTitle( daoUtil.getString( ++nPos ) );

                listBreadcrumbAccordionConfig.add( breadcrumbAccordionConfig );
            }
        }

        return listBreadcrumbAccordionConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByIdForm( int nIdForm )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE + SQL_QUERY_ID_FORM, _plugin ) )
        {
            int nPos = 0;
            daoUtil.setInt( ++nPos, nIdForm );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Creates a {@code BreadcrumbAccordionConfigItem} object from the specified {@code DAOUtil} object
     * 
     * @param daoUtil
     *            the {@code DAOUtil} object
     * @return the created {@code BreadcrumbAccordionConfigItem} object
     */
    private BreadcrumbAccordionConfigItem dataToObject( DAOUtil daoUtil )
    {
        BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem = new BreadcrumbAccordionConfigItem( );
        breadcrumbAccordionConfigItem.setIdStep( daoUtil.getInt( "id_step" ) );
        breadcrumbAccordionConfigItem.setPosition( daoUtil.getInt( "position" ) );
        breadcrumbAccordionConfigItem.setChecked( true );

        return breadcrumbAccordionConfigItem;
    }
}
