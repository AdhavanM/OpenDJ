/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2009 Sun Microsystems, Inc.
 * Portions copyright 2014-2015 ForgeRock AS.
 */
package org.forgerock.opendj.ldap.schema;

import org.forgerock.opendj.ldap.ByteSequence;
import org.forgerock.opendj.ldap.ByteString;
import org.forgerock.opendj.ldap.DecodeException;

import static com.forgerock.opendj.util.StringPrepProfile.*;
import static org.forgerock.opendj.ldap.schema.SchemaConstants.*;

/**
 * This class implements the caseExactIA5SubstringsMatch matching rule. This
 * matching rule actually isn't defined in any official specification, but some
 * directory vendors do provide an implementation using an OID from their own
 * private namespace.
 */
final class CaseExactIA5SubstringMatchingRuleImpl extends AbstractSubstringMatchingRuleImpl {

    CaseExactIA5SubstringMatchingRuleImpl() {
        super(SMR_CASE_EXACT_IA5_NAME, EMR_CASE_EXACT_IA5_NAME);
    }

    @Override
    public ByteString normalizeAttributeValue(final Schema schema, final ByteSequence value)
            throws DecodeException {
        return normalize(TRIM, value);
    }

    @Override
    ByteString normalizeSubString(final Schema schema, final ByteSequence value)
            throws DecodeException {
        return normalize(false, value);
    }

    private ByteString normalize(final boolean trim, final ByteSequence value)
            throws DecodeException {
        return SchemaUtils.normalizeIA5StringAttributeValue(value, trim, NO_CASE_FOLD);
    }
}
