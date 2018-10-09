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

/**
 * This class represents a configuration for a breadcrumb accordion
 *
 */
public class BreadcrumbAccordionConfig
{

    private int _nIdForm;
    private String _strFormTitle;
    private final List<BreadcrumbAccordionConfigItem> _listBreadcrumbAccordionConfigItem;

    /**
     * Constructor
     * 
     * @param nIdForm
     *            the id form
     * @param listBreadcrumbAccordionConfigItem
     *            the list of breadcrumb accordion config items
     */
    public BreadcrumbAccordionConfig( int nIdForm, List<BreadcrumbAccordionConfigItem> listBreadcrumbAccordionConfigItem )
    {
        _nIdForm = nIdForm;
        _listBreadcrumbAccordionConfigItem = listBreadcrumbAccordionConfigItem;
    }

    /**
     * Constructor
     */
    public BreadcrumbAccordionConfig( )
    {
        _nIdForm = 0;
        _listBreadcrumbAccordionConfigItem = new ArrayList<>( );
    }

    /**
     * Gives the id form
     * 
     * @return the id form
     */
    public int getIdForm( )
    {
        return _nIdForm;
    }

    /**
     * Sets the id form
     * 
     * @param nIdForm
     *            the id form to set
     */
    public void setIdForm( int nIdForm )
    {
        _nIdForm = nIdForm;
    }

    /**
     * Gives the list of breadcrumb accordion config items
     * 
     * @return the list of breadcrumb accordion config items
     */
    public List<BreadcrumbAccordionConfigItem> getItems( )
    {
        return _listBreadcrumbAccordionConfigItem;
    }

    /**
     * Adds a breadcrumb accordion config item
     * 
     * @param breadcrumbAccordionConfigItem
     *            the breadcrumb accordion config item to add
     */
    public void add( BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem )
    {
        _listBreadcrumbAccordionConfigItem.add( breadcrumbAccordionConfigItem );
    }

    /**
     * Gives the form title
     * 
     * @return the form title
     */
    public String getFormTitle( )
    {
        return _strFormTitle;
    }

    /**
     * Sets the form title
     * 
     * @param strFormTitle
     *            the form title to set
     */
    public void setFormTitle( String strFormTitle )
    {
        _strFormTitle = strFormTitle;
    }

}
