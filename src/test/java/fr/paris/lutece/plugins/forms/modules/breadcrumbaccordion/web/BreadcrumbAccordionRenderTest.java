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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.forms.business.Step;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfigItem;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web.BreadcrumbAccordion.StepDisplay;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web.BreadcrumbAccordionJspBean.BreadcrumbAccordionConfigItemDisplay;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the templates of the module with HTML auto-escaping (LUT-33153): the HTML of the questions of a completed step,
 * rendered by the forms templates, is kept; the title of a step, typed in the back office, is written as text, in the
 * front office accordion and in the title of the step box of the back office.
 */
public class BreadcrumbAccordionRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_TOP = "skin/plugins/forms/modules/breadcrumbaccordion/breadcrumbaccordion_top.html";
    private static final String TEMPLATE_BOTTOM = "skin/plugins/forms/modules/breadcrumbaccordion/breadcrumbaccordion_bottom.html";
    private static final String TEMPLATE_MODIFY = "admin/plugins/forms/modules/breadcrumbaccordion/modify_breadcrumbaccordion.html";
    /** Stand-ins of the core macros the templates call: they write the title as the core macros do, ${title}. */
    private static final String MACROS = "breadcrumbaccordion_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cTpl rest...><#nested></#macro>"
            + "<#macro cStepDone step title idx rest...><h2 data-step=\"${step}\" data-idx=\"${idx}\">${title}</h2><#nested></#macro>"
            + "<#macro cStepNext step title rest...><h2>${title}</h2></#macro>"
            + "<#macro tform rest...><form><#nested></form></#macro><#macro div rest...><div><#nested></div></#macro>"
            + "<#macro box class='' rest...><div class=\"card ${class}\"><#nested></div></#macro>"
            + "<#macro boxHeader title='' rest...><h2 class=\"card-title\">${title}</h2><#nested></#macro>"
            + "<#macro boxBody rest...><#nested></#macro><#macro formGroup rest...><#nested></#macro>"
            + "<#macro checkBox rest...></#macro><#macro input rest...></#macro><#macro button rest...></#macro><#macro empty rest...></#macro>"
            + "<#macro pageContainer rest...><main><#nested></main></#macro><#macro pageColumn rest...><div><#nested></div></#macro>"
            + "<#macro pageHeader title='' rest...><h1 class=\"page-title\">${title}</h1><#nested></#macro>";

    private static final String TITLE = "Adresse <b>\"l'accueil\"</b>";
    private static final String TITLE_ESCAPED = "Adresse &lt;b&gt;&quot;l&#39;accueil&quot;&lt;/b&gt;";
    private static final String QUESTION_HTML = "<div class=\"form-question\"><p>Réponse</p></div>";

    /**
     * The configuration of AbstractFreeMarkerTemplateService for HTML templates.
     *
     * @return the configuration
     * @throws IOException
     *             if the templates directory cannot be read
     */
    private static Configuration configuration( ) throws IOException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setNumberFormat( "0.######" );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), macros
        } ) );
        cfg.addAutoInclude( MACROS );

        return cfg;
    }

    /**
     * Renders a template of the module.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the rendering
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringWriter writer = new StringWriter( );
        configuration( ).getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * Builds a step.
     *
     * @return the step
     */
    private static Step step( )
    {
        Step step = new Step( );
        step.setId( 7 );
        step.setIdForm( 3 );
        step.setTitle( TITLE );

        return step;
    }

    /**
     * The accordion of the completed steps writes the HTML of their questions as HTML, and the title of the step as
     * text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testCompletedStepKeepsTheQuestionHtmlAndEscapesTheTitle( ) throws Exception
    {
        StepDisplay stepDisplay = new StepDisplay( step( ) );
        Method addHtml = StepDisplay.class.getDeclaredMethod( "addHtml", String.class );
        addHtml.setAccessible( true );
        addHtml.invoke( stepDisplay, QUESTION_HTML );
        Map<String, Object> model = new HashMap<>( );
        model.put( "list_step_completed", List.of( stepDisplay ) );

        String strHtml = render( TEMPLATE_TOP, model );

        assertTrue( strHtml.contains( QUESTION_HTML ), strHtml );
        assertTrue( strHtml.contains( TITLE_ESCAPED ), strHtml );
        assertFalse( strHtml.contains( "<b>" ), strHtml );
    }

    /**
     * A completed step is announced with its number in the form (1, 2...), not with the database id of the step
     * (cStepDone: « Etape {0} réalisée »).
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testCompletedStepsAreNumberedInOrder( ) throws Exception
    {
        Step first = step( );
        first.setId( 41 );
        Step second = step( );
        second.setId( 42 );
        Map<String, Object> model = new HashMap<>( );
        model.put( "list_step_completed", List.of( new StepDisplay( first ), new StepDisplay( second ) ) );

        String strHtml = render( TEMPLATE_TOP, model );

        assertTrue( strHtml.contains( "data-step=\"1\" data-idx=\"0\"" ), strHtml );
        assertTrue( strHtml.contains( "data-step=\"2\" data-idx=\"1\"" ), strHtml );
        assertFalse( strHtml.contains( "data-step=\"41\"" ), strHtml );
    }

    /**
     * The steps still to complete write their title as text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testStepToCompleteEscapesTheTitle( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "list_step_to_complete", List.of( step( ) ) );
        model.put( "current_step_index", 1 );

        String strHtml = render( TEMPLATE_BOTTOM, model );

        assertTrue( strHtml.contains( TITLE_ESCAPED ), strHtml );
        assertFalse( strHtml.contains( "<b>" ), strHtml );
    }

    /**
     * The title of a step box of the back office keeps its strong element and writes the title of the step as text;
     * the box of a step to complete has the class checked, which the script of the page reads; the page has an h1 above
     * the step boxes.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testStepBoxTitleKeepsItsMarkupAndEscapesTheTitle( ) throws Exception
    {
        BreadcrumbAccordionConfigItem configItem = new BreadcrumbAccordionConfigItem( );
        configItem.setChecked( true );
        Map<String, Object> model = new HashMap<>( );
        model.put( "list_breadcrumbAccordion_config_item", List.of( new BreadcrumbAccordionConfigItemDisplay( configItem, step( ) ) ) );

        String strHtml = render( TEMPLATE_MODIFY, model );

        assertTrue( strHtml.contains( "<strong>" + TITLE_ESCAPED + "</strong>" ), strHtml );
        assertFalse( strHtml.contains( "&lt;strong&gt;" ), strHtml );
        assertTrue( strHtml.contains( "<div class=\"card checked\">" ), strHtml );
        assertTrue( strHtml.contains( "<h1 class=\"page-title\">" ), strHtml );
        assertTrue( strHtml.indexOf( "<h1" ) < strHtml.indexOf( "<h2 class=\"card-title\">" ), strHtml );
    }

    /**
     * The page without a step to configure has an h1 too.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testEmptyPageHasAPageHeading( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "list_breadcrumbAccordion_config_item", List.of( ) );

        String strHtml = render( TEMPLATE_MODIFY, model );

        assertTrue( strHtml.contains( "<h1 class=\"page-title\">" ), strHtml );
    }
}
