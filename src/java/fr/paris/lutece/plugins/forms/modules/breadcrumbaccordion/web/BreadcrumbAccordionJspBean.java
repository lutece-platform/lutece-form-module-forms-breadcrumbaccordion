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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.forms.business.Step;
import fr.paris.lutece.plugins.forms.business.StepHome;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfig;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfigItem;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.service.IBreadcrumbAccordionService;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.IEntryTypeService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class represents a component for the BreadcrumbAccordionJspBean
 * {@link fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.web.BreadcrumbAccordionJspBean BreadcrumbAccordionJspBean}
 *
 */
@Controller( controllerJsp = BreadcrumbAccordionJspBean.CONTROLLER_JSP, controllerPath = BreadcrumbAccordionJspBean.CONTROLLER_PATH, right = BreadcrumbAccordionJspBean.RIGHT_MANAGE_BREADCRUMB )
public class BreadcrumbAccordionJspBean extends MVCAdminJspBean
{
    protected static final String RIGHT_MANAGE_BREADCRUMB = "BREADCRUMBACCORDION_MANAGEMENT";
    protected static final String CONTROLLER_JSP = "ManageBreadcrumbAccordion.jsp";
    protected static final String CONTROLLER_PATH = "jsp/admin/plugins/forms/modules/breadcrumbaccordion";
    
    /**
     * Generated serial id
     */
    private static final long serialVersionUID = 3278861033120456895L;

    // View
    private static final String VIEW_MANAGE_BREADCRUMBS = "manageBreadcrumbs";
    private static final String VIEW_MODIFY_BREADCRUMB = "modifyBreadcrumb";

    // Action
    private static final String ACTION_SAVE_CONFIG = "saveConfig";
    private static final String ACTION_CANCEL = "cancel";

    // Parameter
    private static final String PARAMETER_ID_FORM = "id_form";
    private static final String PARAMETER_ID_STEP = "id_step";
    private static final String PARAMETER_STEP_TO_COMPLETE = "step_to_complete";
    private static final String PARAMETER_POSITION = "step_position";

    // Message
    private static final String MESSAGE_MANAGE_BREADCRUMB_ACCORDION_PAGE_TITLE = "module.forms.breadcrumbaccordion.manage_breadcrumbAccordion.page_title";
    private static final String MESSAGE_MODIFY_BREADCRUMB_ACCORDION_PAGE_TITLE = "module.forms.breadcrumbaccordion.modify_breadcrumbAccordion.page_title";

    // Template
    private static final String TEMPLATE_MANAGE_BREADCRUMBS = "admin/plugins/forms/modules/breadcrumbaccordion/manage_breadcrumbaccordion.html";
    private static final String TEMPLATE_MODIFY_BREADCRUMB = "/admin/plugins/forms/modules/breadcrumbaccordion/modify_breadcrumbaccordion.html";

    // Marks
    private static final String MARK_LIST_BREADCRUMB = "list_breadcrumbAccordion";
    private static final String MARK_LIST_BREADCRUMB_CONFIG_ITEM = "list_breadcrumbAccordion_config_item";

    // FIELD
    private static final String FIELD_POSITION = "module.forms.breadcrumbaccordion.modify_breadcrumbAccordion.step.position";

    // Service
    private final transient IBreadcrumbAccordionService _breadcrumbAccordionService = SpringContextService.getBean( IBreadcrumbAccordionService.BEAN_NAME );

    /**
     * Returns the breadcrumb management page
     * 
     * @param request
     *            The request
     * @return the page
     */
    @View( value = VIEW_MANAGE_BREADCRUMBS, defaultView = true )
    public String getManageBreadcrumbs( HttpServletRequest request )
    {

        Map<String, Object> model = getModel( );
        model.put( MARK_LIST_BREADCRUMB, _breadcrumbAccordionService.findBreadcrumbAccordionConfigList( ) );

        setPageTitleProperty( MESSAGE_MANAGE_BREADCRUMB_ACCORDION_PAGE_TITLE );
        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_BREADCRUMBS, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Returns the modify breadcrumb page
     * 
     * @param request
     *            The request
     * @return the page
     */
    @View( value = VIEW_MODIFY_BREADCRUMB )
    public String getModifyBreadcrumb( HttpServletRequest request )
    {
        int nIdForm = NumberUtils.toInt( request.getParameter( PARAMETER_ID_FORM ) );

        Map<String, Object> model = getModel( );

        model.put( PARAMETER_ID_FORM, nIdForm );
        model.put( MARK_LIST_BREADCRUMB_CONFIG_ITEM, createConfigItems( nIdForm ) );

        setPageTitleProperty( MESSAGE_MODIFY_BREADCRUMB_ACCORDION_PAGE_TITLE );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_BREADCRUMB, getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Creates the breadcrumb accordion config items from the specified form id
     * 
     * @param nIdForm
     *            the form id
     * @return the list of breadcrumb accordion config items
     */
    private List<BreadcrumbAccordionConfigItemDisplay> createConfigItems( int nIdForm )
    {
        List<BreadcrumbAccordionConfigItemDisplay> listBreadcrumbAccordionConfigItemDisplay = new ArrayList<>( );
        List<Step> listStep = StepHome.getStepsListByForm( nIdForm );

        List<BreadcrumbAccordionConfigItem> listBreadcrumbAccordionConfigItem = _breadcrumbAccordionService.findbyIdForm( nIdForm ).getItems( );

        for ( Step step : listStep )
        {
            BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem = findConfigItemForStep( step, listBreadcrumbAccordionConfigItem );

            if ( breadcrumbAccordionConfigItem == null )
            {
                breadcrumbAccordionConfigItem = new BreadcrumbAccordionConfigItem( );
            }

            listBreadcrumbAccordionConfigItemDisplay.add( new BreadcrumbAccordionConfigItemDisplay( breadcrumbAccordionConfigItem, step ) );
        }

        return listBreadcrumbAccordionConfigItemDisplay;
    }

    /**
     * Finds the {@code BreadcrumbAccordionConfigItem} object associated to the specified step
     * 
     * @param step
     *            the step
     * @param listBreadcrumbAccordionConfigItem
     *            the list of {@code BreadcrumbAccordionConfigItem} objects
     * @return the found {@code BreadcrumbAccordionConfigItem} of {@code null} if not found
     */
    private BreadcrumbAccordionConfigItem findConfigItemForStep( Step step, List<BreadcrumbAccordionConfigItem> listBreadcrumbAccordionConfigItem )
    {
        BreadcrumbAccordionConfigItem result = null;

        for ( BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem : listBreadcrumbAccordionConfigItem )
        {
            if ( step.getId( ) == breadcrumbAccordionConfigItem.getIdStep( ) )
            {
                result = breadcrumbAccordionConfigItem;
                break;
            }
        }

        return result;
    }

    /**
     * Saves the configuration
     *
     * @param request
     *            The request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_SAVE_CONFIG )
    public String doSaveConfig( HttpServletRequest request )
    {
        int nIdForm = NumberUtils.toInt( request.getParameter( PARAMETER_ID_FORM ) );
        String [ ] listIdStep = request.getParameterValues( PARAMETER_ID_STEP );
        String [ ] listStepToComplete = request.getParameterValues( PARAMETER_STEP_TO_COMPLETE );
        String [ ] listPosition = request.getParameterValues( PARAMETER_POSITION );

        BreadcrumbAccordionConfig breadcrumbAccordionConfig = new BreadcrumbAccordionConfig( );

        breadcrumbAccordionConfig.setIdForm( nIdForm );

        if ( listStepToComplete != null && listStepToComplete.length > 0 )
        {
            List<Integer> listSelectedStepIndex = findSelectedStepIndexes( listStepToComplete, listIdStep );

            if ( !validatePositions( listSelectedStepIndex, listPosition ) )
            {
                Object [ ] listMessageParameters = {
                    I18nService.getLocalizedString( FIELD_POSITION, request.getLocale( ) ),
                };
                redirect( request,
                        AdminMessageService.getMessageUrl( request, IEntryTypeService.MESSAGE_NUMERIC_FIELD, listMessageParameters, AdminMessage.TYPE_STOP ) );
            }

            for ( int nPosition : listSelectedStepIndex )
            {
                BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem = new BreadcrumbAccordionConfigItem( );

                breadcrumbAccordionConfigItem.setIdStep( Integer.parseInt( listIdStep [nPosition] ) );
                breadcrumbAccordionConfigItem.setPosition( Integer.parseInt( listPosition [nPosition] ) );

                breadcrumbAccordionConfig.add( breadcrumbAccordionConfigItem );
            }

            _breadcrumbAccordionService.create( breadcrumbAccordionConfig );
        }
        else
        {
            _breadcrumbAccordionService.removeByIdForm( nIdForm );
        }
        return this.getManageBreadcrumbs( request );
    }

    /**
     * Finds the indexes of the selected steps
     * 
     * @param listStepToComplete
     *            the list of steps to complete
     * @param listIdStep
     *            the list of step ids
     * @return the positions of the selected steps
     */
    private List<Integer> findSelectedStepIndexes( String [ ] listStepToComplete, String... listIdStep )
    {
        List<Integer> listPositions = new ArrayList<>( );

        if ( listStepToComplete != null && listStepToComplete.length > 0 )
        {
            for ( int i = 0; i < listIdStep.length; i++ )
            {
                for ( int j = 0; j < listStepToComplete.length; j++ )
                {
                    if ( listStepToComplete [j].equalsIgnoreCase( listIdStep [i] ) )
                    {
                        listPositions.add( i );
                    }
                }
            }
        }

        return listPositions;
    }

    /**
     * Validates the positions
     * 
     * @param listSelectedStepIndex
     *            the indexes of the selected steps
     * @param listPosition
     *            the list of positions
     * @return {@code true} if the positions are valid, {@code false} otherwise
     */
    private boolean validatePositions( List<Integer> listSelectedStepIndex, String... listPosition )
    {
        boolean bIsValid = true;

        for ( int nPosition : listSelectedStepIndex )
        {
            if ( !validatePosition( listPosition [nPosition] ) )
            {
                bIsValid = false;
                break;
            }
        }

        return bIsValid;
    }

    /**
     * Validates the specified position
     * 
     * @param position
     *            the position to validate
     * @return {@code true} if the position is valid, {@code false} otherwise
     */
    private boolean validatePosition( String position )
    {
        if ( StringUtils.isNotEmpty( position ) )
        {
            try
            {
                Integer.parseInt( position );
            }
            catch( NumberFormatException e )
            {
                return false;
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     * Cancels the modification
     *
     * @param request
     *            The request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CANCEL )
    public String doCancel( HttpServletRequest request )
    {
        return getManageBreadcrumbs( request );
    }

    /**
     * This class is used to display a {@code the {@code BreadcrumbAccordionConfigItem} object
     *
     */
    public static final class BreadcrumbAccordionConfigItemDisplay
    {
        private final BreadcrumbAccordionConfigItem _breadcrumbAccordionConfigItem;
        private final Step _step;

        /**
         * Constructor
         * 
         * @param breadcrumbAccordionConfigItem
         *            the breadcrumb accordion config item to display
         * @param step
         *            the associated step
         */
        BreadcrumbAccordionConfigItemDisplay( BreadcrumbAccordionConfigItem breadcrumbAccordionConfigItem, Step step )
        {
            _breadcrumbAccordionConfigItem = breadcrumbAccordionConfigItem;
            _step = step;
        }

        /**
         * Gives the {@code BreadcrumbAccordionConfigItem} object
         * 
         * @return the {@code BreadcrumbAccordionConfigItem} object
         */
        public BreadcrumbAccordionConfigItem getConfigItem( )
        {
            return _breadcrumbAccordionConfigItem;
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
    }
}
