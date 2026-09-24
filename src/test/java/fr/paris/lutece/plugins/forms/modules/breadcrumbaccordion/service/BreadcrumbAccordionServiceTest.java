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
package fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.BreadcrumbAccordionConfig;
import fr.paris.lutece.plugins.forms.modules.breadcrumbaccordion.business.IBreadcrumbAccordionDAO;
import fr.paris.lutece.plugins.forms.service.cache.FormsCacheService;

/**
 * Tests the breadcrumb accordion configuration cache.
 */
public class BreadcrumbAccordionServiceTest
{
    private static final int FORM_ID = 123;
    private static final String CONFIG_CACHE_KEY = "BreadcrumbAccordionConfig-Form-id:" + FORM_ID;

    private BreadcrumbAccordionService _service;
    private BreadcrumbAccordionDAOStub _dao;
    private FormsCacheServiceStub _cache;

    /**
     * Initializes the service and its test doubles.
     *
     * @throws Exception
     *             if a service dependency cannot be injected
     */
    @BeforeEach
    public void setUp( ) throws Exception
    {
        _service = new BreadcrumbAccordionService( );
        _dao = new BreadcrumbAccordionDAOStub( );
        _cache = new FormsCacheServiceStub( );

        inject( _service, "_breadcrumbAccordionDAO", _dao );
        inject( _service, "_formsCacheService", _cache );
    }

    /**
     * Verifies that a configuration is retrieved from the DAO once then reused from the Forms cache.
     */
    @Test
    public void testFindByIdFormCachesConfiguration( )
    {
        BreadcrumbAccordionConfig breadcrumbAccordionConfig = new BreadcrumbAccordionConfig( );
        _dao._breadcrumbAccordionConfig = breadcrumbAccordionConfig;

        assertSame( breadcrumbAccordionConfig, _service.findbyIdForm( FORM_ID ) );
        assertSame( breadcrumbAccordionConfig, _service.findbyIdForm( FORM_ID ) );
        assertEquals( 1, _dao._nSelectByIdFormCalls );
        assertSame( breadcrumbAccordionConfig, _cache._cache.get( CONFIG_CACHE_KEY ) );
    }

    /**
     * Verifies that saving a configuration invalidates its cached value.
     */
    @Test
    public void testCreateInvalidatesConfigurationCache( )
    {
        BreadcrumbAccordionConfig breadcrumbAccordionConfig = new BreadcrumbAccordionConfig( );
        breadcrumbAccordionConfig.setIdForm( FORM_ID );
        _cache._cache.put( CONFIG_CACHE_KEY, new BreadcrumbAccordionConfig( ) );

        _service.create( breadcrumbAccordionConfig );

        assertSame( breadcrumbAccordionConfig, _dao._insertedConfig );
        assertNull( _cache._cache.get( CONFIG_CACHE_KEY ) );
    }

    /**
     * Verifies that removing a configuration invalidates its cached value.
     */
    @Test
    public void testRemoveByIdFormInvalidatesConfigurationCache( )
    {
        _cache._cache.put( CONFIG_CACHE_KEY, new BreadcrumbAccordionConfig( ) );

        _service.removeByIdForm( FORM_ID );

        assertEquals( FORM_ID, _dao._nDeletedFormId );
        assertNull( _cache._cache.get( CONFIG_CACHE_KEY ) );
    }

    /**
     * Injects a test dependency in the service.
     *
     * @param target
     *            the target service
     * @param strFieldName
     *            the field name
     * @param value
     *            the dependency to inject
     * @throws Exception
     *             if the field cannot be accessed
     */
    private void inject( Object target, String strFieldName, Object value ) throws Exception
    {
        Field field = target.getClass( ).getDeclaredField( strFieldName );
        field.setAccessible( true );
        field.set( target, value );
    }

    /**
     * DAO test double that records calls made by the service.
     */
    private static final class BreadcrumbAccordionDAOStub implements IBreadcrumbAccordionDAO
    {
        private BreadcrumbAccordionConfig _breadcrumbAccordionConfig;
        private BreadcrumbAccordionConfig _insertedConfig;
        private int _nSelectByIdFormCalls;
        private int _nDeletedFormId;

        /**
         * {@inheritDoc}
         */
        @Override
        public BreadcrumbAccordionConfig selectByIdForm( int nIdForm )
        {
            _nSelectByIdFormCalls++;
            return _breadcrumbAccordionConfig;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<BreadcrumbAccordionConfig> selectBreadcrumbAccordionConfigList( )
        {
            return Collections.emptyList( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insert( BreadcrumbAccordionConfig breadcrumbAccordionConfig )
        {
            _insertedConfig = breadcrumbAccordionConfig;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void deleteByIdForm( int nIdForm )
        {
            _nDeletedFormId = nIdForm;
        }
    }

    /**
     * Forms cache test double backed by an in-memory map.
     */
    private static final class FormsCacheServiceStub extends FormsCacheService
    {
        private final Map<String, Object> _cache = new HashMap<>( );

        /**
         * {@inheritDoc}
         */
        @Override
        public Object get( String strKey )
        {
            return _cache.get( strKey );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void put( String strKey, Object object )
        {
            _cache.put( strKey, object );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean remove( String strKey )
        {
            return _cache.remove( strKey ) != null;
        }
    }
}
