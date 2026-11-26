/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfigItem;

/**
 * Saving the accordion configuration with a step id or a position that is not a number must be refused (stop message) instead
 * of a NumberFormatException (500, c4h e2e signatures S39/S63 « For input string: "abc" » at doSaveConfig:270-271). The action
 * also went on after its refusal redirect, because the redirect was not returned.
 */
public class BreadcrumbAccordionSaveConfigTest
{
    private static final String [ ] STEPS = { "5", "6", "7" };

    @Test
    public void testNonNumericValuesOfASelectedStepAreRefused( )
    {
        assertNull( BreadcrumbAccordionJspBean.buildConfigItems( new String [ ] { "abc", "6" }, new String [ ] { "abc" }, new String [ ] { "1", "2" } ),
                "step id abc" );
        assertNull( BreadcrumbAccordionJspBean.buildConfigItems( STEPS, new String [ ] { "6" }, new String [ ] { "1", "abc", "3" } ), "position abc" );
        assertNull( BreadcrumbAccordionJspBean.buildConfigItems( STEPS, new String [ ] { "6" }, new String [ ] { "1", "*", "3" } ), "position *" );
        assertNull( BreadcrumbAccordionJspBean.buildConfigItems( STEPS, new String [ ] { "6" }, new String [ ] { "1", "", "3" } ), "empty position" );
        assertNull( BreadcrumbAccordionJspBean.buildConfigItems( STEPS, new String [ ] { "7" }, new String [ ] { "1", "2" } ), "missing position" );
    }

    @Test
    public void testValidValuesGiveTheItemsOfTheSelectedSteps( )
    {
        // a non numeric position of a step that is not selected does not matter
        List<BreadcrumbAccordionConfigItem> listItems = BreadcrumbAccordionJspBean.buildConfigItems( STEPS, new String [ ] { "5", "7" },
                new String [ ] { "2", "x", "1" } );

        assertEquals( 2, listItems.size( ) );
        assertEquals( 5, listItems.get( 0 ).getIdStep( ) );
        assertEquals( 2, listItems.get( 0 ).getPosition( ) );
        assertEquals( 7, listItems.get( 1 ).getIdStep( ) );
        assertEquals( 1, listItems.get( 1 ).getPosition( ) );
    }
}
