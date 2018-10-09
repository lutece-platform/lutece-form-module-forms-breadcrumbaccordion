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

import fr.paris.lutece.plugins.forms.business.Step;

/**
 * This class represents a configuration item for a breadcrumb accordion
 *
 */
public class BreadcrumbAccordionConfigItem
{

    private int _nIdStep;
    private int _nPosition;
    private boolean _bIsChecked;
    private Step _step;

    /**
     * Gives the step id
     * 
     * @return the step id
     */
    public int getIdStep( )
    {
        return _nIdStep;
    }

    /**
     * Sets the step id
     * 
     * @param nIdStep
     *            the step id to set
     */
    public void setIdStep( int nIdStep )
    {
        _nIdStep = nIdStep;
    }

    /**
     * Gives the position
     * 
     * @return the position
     */
    public int getPosition( )
    {
        return _nPosition;
    }

    /**
     * Sets the position
     * 
     * @param nPosition
     *            the position to set
     */
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }

    /**
     * Tests if the config item is checked
     * 
     * @return {@code true} if the item is checked, {@code false} otherwise
     */
    public boolean isChecked( )
    {
        return _bIsChecked;
    }

    /**
     * Sets if the config item is checked or not
     * 
     * @param bIsChecked
     *            {@code true} if the item is checked, {@code false} otherwise
     */
    public void setChecked( boolean bIsChecked )
    {
        _bIsChecked = bIsChecked;
    }

    /**
     * Gives the step
     * 
     * @return the step
     */
    public Step getStep( )
    {
        return _step;
    }

    /**
     * Sets the step
     * 
     * @param step
     *            the step to set
     */
    public void setStep( Step step )
    {
        _step = step;
    }

}
