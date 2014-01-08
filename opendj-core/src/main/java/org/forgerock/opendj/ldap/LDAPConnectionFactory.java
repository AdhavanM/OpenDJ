/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2009-2010 Sun Microsystems, Inc.
 *      Portions copyright 2011-2014 ForgeRock AS.
 */

package org.forgerock.opendj.ldap;

import static com.forgerock.opendj.util.StaticUtils.getProvider;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.forgerock.opendj.ldap.spi.LDAPConnectionFactoryImpl;
import org.forgerock.opendj.ldap.spi.TransportProvider;
import org.forgerock.util.Reject;

/**
 * A factory class which can be used to obtain connections to an LDAP Directory
 * Server.
 */
public final class LDAPConnectionFactory implements ConnectionFactory {
    /*
     * We implement the factory using the pimpl idiom in order to avoid making
     * too many implementation classes public.
     */
    private final LDAPConnectionFactoryImpl impl;

    /*
     * Transport provider that provides the implementation of this factory.
     */
    private TransportProvider provider;

    /**
     * Creates a new LDAP connection factory which can be used to create LDAP
     * connections to the Directory Server at the provided address.
     *
     * @param address
     *            The address of the Directory Server.
     * @throws NullPointerException
     *             If {@code address} was {@code null}.
     * @throws ProviderNotFoundException if no provider is available or if the
     *             provider requested using options is not found.
     */
    public LDAPConnectionFactory(final InetSocketAddress address) {
        this(address, new LDAPOptions());
    }

    /**
     * Creates a new LDAP connection factory which can be used to create LDAP
     * connections to the Directory Server at the provided address.
     *
     * @param address
     *            The address of the Directory Server.
     * @param options
     *            The LDAP options to use when creating connections.
     * @throws NullPointerException
     *             If {@code address} or {@code options} was {@code null}.
     * @throws ProviderNotFoundException if no provider is available or if the
     *             provider requested using options is not found.
     */
    public LDAPConnectionFactory(final InetSocketAddress address, final LDAPOptions options) {
        Reject.ifNull(address, options);
        this.provider = getProvider(TransportProvider.class, options.getTransportProvider(),
                options.getProviderClassLoader());
        this.impl = provider.getLDAPConnectionFactory(address, options);
    }

    /**
     * Creates a new LDAP connection factory which can be used to create LDAP
     * connections to the Directory Server at the provided host and port
     * address.
     *
     * @param host
     *            The host name.
     * @param port
     *            The port number.
     * @throws NullPointerException
     *             If {@code host} was {@code null}.
     * @throws ProviderNotFoundException if no provider is available or if the
     *             provider requested using options is not found.
     */
    public LDAPConnectionFactory(final String host, final int port) {
        this(host, port, new LDAPOptions());
    }

    /**
     * Creates a new LDAP connection factory which can be used to create LDAP
     * connections to the Directory Server at the provided host and port
     * address.
     *
     * @param host
     *            The host name.
     * @param port
     *            The port number.
     * @param options
     *            The LDAP options to use when creating connections.
     * @throws NullPointerException
     *             If {@code host} or {@code options} was {@code null}.
     * @throws ProviderNotFoundException if no provider is available or if the
     *             provider requested using options is not found.
     */
    public LDAPConnectionFactory(final String host, final int port, final LDAPOptions options) {
        Reject.ifNull(host, options);
        final InetSocketAddress address = new InetSocketAddress(host, port);
        this.provider = getProvider(TransportProvider.class, options.getTransportProvider(),
                options.getProviderClassLoader());
        this.impl = provider.getLDAPConnectionFactory(address, options);
    }

    /**
     * Returns the {@code InetAddress} of the Directory Server.
     *
     * @return The {@code InetAddress} of the Directory Server.
     */
    public InetAddress getAddress() {
        return getSocketAddress().getAddress();
    }

    @Override
    public void close() {
        impl.close();
    }

    @Override
    public FutureResult<Connection> getConnectionAsync(
            final ResultHandler<? super Connection> handler) {
        return impl.getConnectionAsync(handler);
    }

    @Override
    public Connection getConnection() throws ErrorResultException {
        return impl.getConnection();
    }

    /**
     * Returns the host name of the Directory Server. The returned host name is
     * the same host name that was provided during construction and may be an IP
     * address. More specifically, this method will not perform a reverse DNS
     * lookup.
     *
     * @return The host name of the Directory Server.
     */
    public String getHostName() {
        return Connections.getHostString(getSocketAddress());
    }

    /**
     * Returns the port of the Directory Server.
     *
     * @return The port of the Directory Server.
     */
    public int getPort() {
        return getSocketAddress().getPort();
    }

    /**
     * Returns the address of the Directory Server.
     *
     * @return The address of the Directory Server.
     */
    public InetSocketAddress getSocketAddress() {
        return impl.getSocketAddress();
    }

    /**
     * Returns the name of the transport provider, which provides the implementation
     * of this factory.
     *
     * @return The name of actual transport provider.
     */
    public String getProviderName() {
        return provider.getName();
    }

    @Override
    public String toString() {
        return impl.toString();
    }
}
