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
package fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import fr.paris.lutece.plugins.forms.business.Step;
import fr.paris.lutece.plugins.forms.business.StepHome;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfig;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfigItem;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.IBreadcrumbAccordionDAO;

/**
 * This class is a service for the breadcrumb accordion
 *
 */
@ApplicationScoped
public class BreadcrumbAccordionService implements IBreadcrumbAccordionService
{
    @Inject
    private IBreadcrumbAccordionDAO _breadcrumbAccordionDAO;

    private final BreadcrumbAccordionConfigItemComparator _breadcrumbAccordionConfigItemComparator = new BreadcrumbAccordionConfigItemComparator( );

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadcrumbAccordionConfig findbyIdForm( int nIdForm )
    {
        return _breadcrumbAccordionDAO.selectByIdForm( nIdForm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BreadcrumbAccordionConfig> findBreadcrumbAccordionConfigList( )
    {
        return _breadcrumbAccordionDAO.selectBreadcrumbAccordionConfigList( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Step> findStepsToComplete( int nIdForm )
    {
        List<Step> listStepToComplete = new ArrayList<>( );

        BreadcrumbAccordionConfig breadcrumbAccordionConfig = _breadcrumbAccordionDAO.selectByIdForm( nIdForm );

        List<BreadcrumbAccordionConfigItem> listBreadcrumbAccordionConfigItem = breadcrumbAccordionConfig.getItems( );

        Collections.sort( listBreadcrumbAccordionConfigItem, _breadcrumbAccordionConfigItemComparator );

        for ( BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem : listBreadcrumbAccordionConfigItem )
        {
            listStepToComplete.add( StepHome.findByPrimaryKey( breadcrumbAccordionConfigItem.getIdStep( ) ) );
        }

        return listStepToComplete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create( BreadcrumbAccordionConfig breadcrumbAccordionConfig )
    {
        _breadcrumbAccordionDAO.insert( breadcrumbAccordionConfig );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByIdForm( int nIdForm )
    {
        _breadcrumbAccordionDAO.deleteByIdForm( nIdForm );
    }

    /**
     * This class represents a comparator for {@code BreadcrumbAccordionConfigItem} objects
     *
     */
    private static final class BreadcrumbAccordionConfigItemComparator implements Comparator<BreadcrumbAccordionConfigItem>, Serializable
    {
        /**
         * Generated serial id
         */
        private static final long serialVersionUID = -5984616322679495680L;

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare( BreadcrumbAccordionConfigItem configItem1, BreadcrumbAccordionConfigItem configItem2 )
        {
            return Integer.compare( configItem1.getPosition( ), configItem2.getPosition( ) );
        }

    }
}
