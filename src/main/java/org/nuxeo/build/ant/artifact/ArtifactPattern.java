/*
 * (C) Copyright 2006-2015 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu, jcarsique
 */
package org.nuxeo.build.ant.artifact;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.types.DataType;
import org.eclipse.aether.util.artifact.JavaScopes;

import org.nuxeo.build.maven.filter.AncestorFilter;
import org.nuxeo.build.maven.filter.AndFilter;
import org.nuxeo.build.maven.filter.ArtifactIdFilter;
import org.nuxeo.build.maven.filter.ClassifierFilter;
import org.nuxeo.build.maven.filter.Filter;
import org.nuxeo.build.maven.filter.GroupIdFilter;
import org.nuxeo.build.maven.filter.IsOptionalFilter;
import org.nuxeo.build.maven.filter.ManifestBundleCategoryFilter;
import org.nuxeo.build.maven.filter.NotFilter;
import org.nuxeo.build.maven.filter.ScopeFilter;
import org.nuxeo.build.maven.filter.TypeFilter;
import org.nuxeo.build.maven.filter.VersionFilter;

/**
 * TODO NXBT-258
 */
@SuppressWarnings("deprecation")
public class ArtifactPattern extends DataType {

    private AndFilter filter = new AndFilter();

    protected String category = null;

    protected String groupId = null;

    protected String artifactId = null;

    protected String version = null;

    protected String classifier = null;

    protected String type = null;

    protected String scope = "!test";

    protected boolean isOptional = false;

    protected String pattern = null;

    protected String ancestor = null;

    protected boolean isDependsOnCategory = true;

    @Deprecated
    private ManifestBundleCategoryFilter categoryFilter = null;

    public Filter getFilter() {
        return filter;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        filter.addFilter(GroupIdFilter.class, groupId);
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        filter.addFilter(ArtifactIdFilter.class, artifactId);
    }

    public void setVersion(String version) {
        this.version = version;
        filter.addFilter(VersionFilter.class, version);
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
        filter.addFilter(ClassifierFilter.class, classifier);
    }

    public void setType(String type) {
        this.type = type;
        filter.addFilter(TypeFilter.class, type);
    }

    public void setScope(String scope) {
        this.scope = scope;
        if (StringUtils.isBlank(scope)) { // Exclude test and provided scopes by default
            filter.addFilter(new NotFilter(new ScopeFilter(JavaScopes.TEST)));
            filter.addFilter(new NotFilter(new ScopeFilter(JavaScopes.PROVIDED)));
        } else if (!"*".equals(scope)) {
            filter.addFilter(ScopeFilter.class, scope);
        }
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
        if (isOptional) {
            filter.addFilter(new IsOptionalFilter(isOptional));
        }
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        filter.addFiltersFromPattern(pattern);
    }

    public void setAncestor(String ancestor) {
        this.ancestor = ancestor;
        filter.addFilter(AncestorFilter.class, ancestor);
    }

    /**
     * @deprecated since 2.0
     */
    @Deprecated
    public void setCategory(String category) {
        this.category = category;
        categoryFilter = new ManifestBundleCategoryFilter(category, isDependsOnCategory);
        filter.addFilter(categoryFilter);
    }

    /**
     * @deprecated since 2.0
     */
    @Deprecated
    public void setDependsOnCategory(boolean isDependsOnCategory) {
        this.isDependsOnCategory = isDependsOnCategory;
        // in case category has been set before isDependsOnCategory and
        // isDependsOnCategory==false
        if (categoryFilter != null && !isDependsOnCategory) {
            categoryFilter.setDependsOnCategory(false);
        }
    }

}
