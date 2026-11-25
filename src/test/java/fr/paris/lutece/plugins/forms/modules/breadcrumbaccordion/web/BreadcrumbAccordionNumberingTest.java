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

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.forms.business.Step;

/**
 * Number of the current step in the accordion, when every step is in the configuration and when the current step is not
 * (step not ticked, or created after the configuration was saved)
 */
public class BreadcrumbAccordionNumberingTest
{
    private static Step step( int nId )
    {
        Step step = new Step( );
        step.setId( nId );
        return step;
    }

    private static int number( Step current, List<Step> validated, List<Step> configured )
    {
        return BreadcrumbAccordion.currentStepIndex( current, validated, configured, BreadcrumbAccordion.findNotCompletedSteps( validated, configured ) );
    }

    @Test
    public void testEveryStepConfigured( )
    {
        Step s1 = step( 1 );
        Step s2 = step( 2 );
        Step s3 = step( 3 );
        List<Step> configured = List.of( s1, s2, s3 );

        assertEquals( 1, number( s1, List.of( s1 ), configured ) );
        assertEquals( 2, number( s2, List.of( s1, s2 ), configured ) );
        assertEquals( 3, number( s3, List.of( s1, s2, s3 ), configured ) );
    }

    /**
     * S2 not ticked: on S2, after S1 (number 1), the current step is number 2, not 1 again; S3 after them is number 3
     */
    @Test
    public void testCurrentStepNotConfigured( )
    {
        Step s1 = step( 1 );
        Step s2 = step( 2 );
        Step s3 = step( 3 );
        List<Step> configured = List.of( s1, s3 );

        assertEquals( 1, number( s1, List.of( s1 ), configured ) );
        assertEquals( 2, number( s2, List.of( s1, s2 ), configured ) );
        assertEquals( 3, number( s3, List.of( s1, s2, s3 ), configured ) );
    }
}
