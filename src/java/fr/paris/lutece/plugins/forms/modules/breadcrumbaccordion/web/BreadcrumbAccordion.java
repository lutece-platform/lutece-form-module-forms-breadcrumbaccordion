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
package fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.forms.business.FormDisplay;
import fr.paris.lutece.plugins.forms.business.FormDisplayHome;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.Step;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.service.BreadcrumbAccordionService;
import fr.paris.lutece.plugins.forms.web.CompositeQuestionDisplay;
import fr.paris.lutece.plugins.forms.web.FormResponseManager;
import fr.paris.lutece.plugins.forms.web.breadcrumb.IBreadcrumb;
import fr.paris.lutece.plugins.forms.web.entrytype.DisplayType;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class is a Breadcrumb Accordion
 *
 */
public class BreadcrumbAccordion implements IBreadcrumb
{

    public static final String BEAN_NAME = "forms-breadcrumbaccordion.breadcrumbAccordion";

    private static final String MARK_LIST_STEP_COMPLETED = "list_step_completed";
    private static final String MARK_LIST_STEP_TO_COMPLETE = "list_step_to_complete";
    private static final String MARK_CURRENT_STEP_INDEX = "current_step_index";
    private static final String MARK_CURRENT_STEP = "current_step";
    private static final String MARK_IS_BREADCRUMB_ACCORDION = "is_breadcrumb_accordion";

    private static final String TEMPLATE_BREADCRUMB_TOP_HTML = "/skin/plugins/forms/modules/breadcrumbaccordion/breadcrumbaccordion_top.html";
    private static final String TEMPLATE_BREADCRUMB_BOTTOM_HTML = "/skin/plugins/forms/modules/breadcrumbaccordion/breadcrumbaccordion_bottom.html";
    private final String _strBreadcrumbAccordionBeanName;
    private final String _strBreadcrumbAccordionDisplayBeanName;

    // Service
    @Inject
    private BreadcrumbAccordionService _breadcrumbAccordionService;

    /**
     * Constructor
     * 
     * @param strBreadcrumbAccordionName
     *            The breadcrumbmd bean name
     * @param strBreadcrumbAccordionDisplayName
     *            The Breadcrumbmd display name
     */
    public BreadcrumbAccordion( String strBreadcrumbAccordionName, String strBreadcrumbAccordionDisplayName )
    {
        _strBreadcrumbAccordionBeanName = strBreadcrumbAccordionName;
        _strBreadcrumbAccordionDisplayBeanName = I18nService.getLocalizedString( strBreadcrumbAccordionDisplayName, I18nService.getDefaultLocale( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBreadcrumbBeanName( )
    {
        return _strBreadcrumbAccordionBeanName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBreadcrumbDisplayName( )
    {
        return _strBreadcrumbAccordionDisplayBeanName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTopHtml( HttpServletRequest request, FormResponseManager formResponseManager )
    {
        List<Step> listValidatedStep = formResponseManager.getValidatedSteps( );
        Step stepCurrent = listValidatedStep.get( formResponseManager.getValidatedSteps( ).size( ) - 1 );
        listValidatedStep.remove( listValidatedStep.size( ) - 1 );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_STEP_COMPLETED, createStepDisplays( request, listValidatedStep, formResponseManager ) );
        model.put( MARK_CURRENT_STEP, stepCurrent );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BREADCRUMB_TOP_HTML, request.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getModelForCurrentStep( HttpServletRequest request, FormResponseManager formResponseManager )
    {
        Map<String, Object> model = new HashMap<>( );

        Step stepCurrent = formResponseManager.getValidatedSteps( ).get( formResponseManager.getValidatedSteps( ).size( ) - 1 );
        List<Step> listStepToComplete = _breadcrumbAccordionService.findStepsToComplete( stepCurrent.getIdForm( ) );
        List<Step> listNotCompletedStep = findNotCompletedSteps( formResponseManager.getValidatedSteps( ), listStepToComplete );
        int nCurrentStepIndex = listStepToComplete.size( ) - listNotCompletedStep.size( );

        model.put( MARK_CURRENT_STEP_INDEX, nCurrentStepIndex );

        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBottomHtml( HttpServletRequest request, FormResponseManager formResponseManager )
    {
        Step stepCurrent = formResponseManager.getValidatedSteps( ).get( formResponseManager.getValidatedSteps( ).size( ) - 1 );

        List<Step> listStepToComplete = _breadcrumbAccordionService.findStepsToComplete( stepCurrent.getIdForm( ) );
        List<Step> listNotCompletedStep = findNotCompletedSteps( formResponseManager.getValidatedSteps( ), listStepToComplete );
        int nCurrentStepIndex = listStepToComplete.size( ) - listNotCompletedStep.size( );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_STEP_TO_COMPLETE, listNotCompletedStep );
        model.put( MARK_CURRENT_STEP_INDEX, nCurrentStepIndex );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BREADCRUMB_BOTTOM_HTML, request.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Finds the not completed steps
     * 
     * @param listStepValidated
     *            the list of validated steps
     * @param listStepToComplete
     *            the list of steps to complete
     * @return the not completed steps
     */
    private List<Step> findNotCompletedSteps( List<Step> listStepValidated, List<Step> listStepToComplete )
    {
        List<Step> listNotCompletedStep = new ArrayList<>( );

        for ( int i = listStepToComplete.size( ) - 1; i >= 0; i-- )
        {
            boolean bFound = false;
            Step stepToComplete = listStepToComplete.get( i );

            for ( Step stepValidated : listStepValidated )
            {
                if ( stepToComplete.getId( ) == stepValidated.getId( ) )
                {
                    bFound = true;
                }
            }

            if ( bFound )
            {
                break;
            }
            else
            {
                listNotCompletedStep.add( stepToComplete );
            }
        }

        Collections.reverse( listNotCompletedStep );

        return listNotCompletedStep;
    }

    /**
     * Creates the {@code StepDisplay} objects for the specified list of steps
     * 
     * @param request
     *            the request
     * @param listStep
     *            the steps
     * @param formResponseManager
     *            the form response manager
     * @return the {@code StepDisplay} objects
     */
    private List<StepDisplay> createStepDisplays( HttpServletRequest request, List<Step> listStep, FormResponseManager formResponseManager )
    {
        List<StepDisplay> listStepDisplay = new ArrayList<>( );

        for ( Step step : listStep )
        {
            StepDisplay stepDisplay = new StepDisplay( step );

            for ( FormQuestionResponse formQuestionResponse : formResponseManager.findResponsesFor( step ) )
            {
                stepDisplay.addHtml( buildHtmlForQuestion( request, formResponseManager.getFormResponse( ), formQuestionResponse, request.getLocale( ) ) );
            }

            listStepDisplay.add( stepDisplay );
        }

        return listStepDisplay;
    }

    /**
     * Builds the HTML for the specified question
     * 
     * @param request
     *            the request
     * @param formResponse
     *            the form response
     * @param formQuestionResponse
     *            the form response associated to the questions
     * @param locale
     *            the locale
     * @return the HTML
     */
    private String buildHtmlForQuestion( HttpServletRequest request, FormResponse formResponse, FormQuestionResponse formQuestionResponse, Locale locale )
    {
        Question question = formQuestionResponse.getQuestion( );
        FormDisplay formDisplayQuestion = FormDisplayHome.getFormDisplayByFormStepAndComposite( formResponse.getFormId( ), question.getIdStep( ),
                question.getId( ) );
        CompositeQuestionDisplay compositeQuestionDisplay = new CompositeQuestionDisplay( formDisplayQuestion, formResponse, question.getIterationNumber( ) );
        List<FormQuestionResponse> listFormQuestionResponse = new ArrayList<>( );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_IS_BREADCRUMB_ACCORDION, true );
        compositeQuestionDisplay.addModel( model );

        listFormQuestionResponse.add( formQuestionResponse );

        return compositeQuestionDisplay.getCompositeHtml( request, listFormQuestionResponse, locale, DisplayType.READONLY_FRONTOFFICE );
    }

    /**
     * This class is used to display a step
     *
     */
    public static final class StepDisplay
    {
        private final Step _step;
        private final List<String> _listQuestionHtml;

        /**
         * Constructor
         * 
         * @param step
         *            the step to display
         */
        StepDisplay( Step step )
        {
            _step = step;
            _listQuestionHtml = new ArrayList<>( );
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
         * Adds HTML for a question
         * 
         * @param strHtml
         *            the HTML to add
         */
        private void addHtml( String strHtml )
        {
            _listQuestionHtml.add( strHtml );
        }

        /**
         * Gives the HTML of the questions
         * 
         * @return the HTML of the questions
         */
        public List<String> getQuestionHtml( )
        {
            return _listQuestionHtml;
        }
    }

}
