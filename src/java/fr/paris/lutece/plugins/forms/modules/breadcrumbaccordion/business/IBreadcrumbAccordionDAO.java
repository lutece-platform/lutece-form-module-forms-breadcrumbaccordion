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

import java.util.List;

/**
 * This class represents a Data Access Object for a SQL database for {@code BreadcrumbAccordion} objects
 *
 */
public interface IBreadcrumbAccordionDAO
{
    /**
     * Loads the {@code breadcrumbAccordionConfig} object from the table by the specified form id
     * 
     * @param nIdForm
     *            The identifier of the form
     * @return The {@code breadcrumbAccordionConfig} object
     */
    BreadcrumbAccordionConfig selectByIdForm( int nIdForm );

    /**
     * Loads the data of all the {@code breadcrumbAccordionConfig} objects and returns them as a list
     * 
     * @return The list which contains the data of all the {@code breadcrumbAccordionConfig} objects
     */
    List<BreadcrumbAccordionConfig> selectBreadcrumbAccordionConfigList( );

    /**
     * Inserts a new record in the table.
     * 
     * @param breadcrumbAccordionConfig
     *            the {@code breadcrumbAccordionConfig} object to insert
     */
    void insert( BreadcrumbAccordionConfig breadcrumbAccordionConfig );

    /**
     * Deletes a record from the table for the specified form id
     * 
     * @param nIdForm
     *            The form id
     */
    void deleteByIdForm( int nIdForm );
}
