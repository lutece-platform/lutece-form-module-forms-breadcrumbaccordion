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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;

/**
 * The accordion configuration of a form is offered only to the administrators of the workgroup of the form
 */
public class BreadcrumbAccordionWorkgroupTest
{
    private static Form form( String strWorkgroup )
    {
        Form form = new Form( );
        form.setWorkgroup( strWorkgroup );
        return form;
    }

    @Test
    public void testWorkgroupOfTheForm( )
    {
        AdminUser user = new AdminUser( );
        user.setUserId( 987654 );
        user.setUserWorkgroups( List.of( "e2e_lb07_mine" ) );

        assertTrue( BreadcrumbAccordionJspBean.isFormAuthorized( form( AdminWorkgroupService.ALL_GROUPS ), user ), "form of all workgroups" );
        assertFalse( BreadcrumbAccordionJspBean.isFormAuthorized( form( "e2e_lb07_not_mine" ), user ), "form of another workgroup" );
        assertTrue( BreadcrumbAccordionJspBean.isFormAuthorized( form( "e2e_lb07_mine" ), user ), "form of his workgroup" );
        assertFalse( BreadcrumbAccordionJspBean.isFormAuthorized( null, user ), "form that does not exist" );
    }
}
