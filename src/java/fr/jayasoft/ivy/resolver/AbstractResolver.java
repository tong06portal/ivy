/*
 * This file is subject to the license found in LICENCE.TXT in the root directory of the project.
 * 
 * #SNAPSHOT#
 */
package fr.jayasoft.ivy.resolver;

import fr.jayasoft.ivy.Artifact;
import fr.jayasoft.ivy.DependencyResolver;
import fr.jayasoft.ivy.Ivy;
import fr.jayasoft.ivy.IvyAware;
import fr.jayasoft.ivy.LatestStrategy;
import fr.jayasoft.ivy.ResolveData;
import fr.jayasoft.ivy.namespace.Namespace;
import fr.jayasoft.ivy.report.ArtifactDownloadReport;
import fr.jayasoft.ivy.report.DownloadReport;
import fr.jayasoft.ivy.report.DownloadStatus;
import fr.jayasoft.ivy.util.Message;

/**
 * This abstract resolver only provides handling for resolver name
 */
public abstract class AbstractResolver implements DependencyResolver, IvyAware, HasLatestStrategy {

    /**
     * True if parsed ivy files should be validated against xsd, false if they should not,
     * null if default behaviour should be used
     */
    private Boolean _validate = null;
    private String _name;
    private Ivy _ivy;

    /**
     * The latest strategy to use to find latest among several artifacts
     */
    private LatestStrategy _latestStrategy;

    private String _latestStrategyName;

    /**
     * The namespace to which this resolver belongs
     */
    private Namespace _namespace;

    private String _namespaceName;

    public Ivy getIvy() {
        return _ivy;
    }    

    public void setIvy(Ivy ivy) {
        _ivy = ivy;
    }
    
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    protected boolean doValidate(ResolveData data) {
        if (_validate != null) {
            return _validate.booleanValue();
        } else {
            return data.isValidate();
        }
    }

    public boolean isValidate() {
        return _validate == null ? true: _validate.booleanValue();
    }
    

    public void setValidate(boolean validate) {
        _validate = Boolean.valueOf(validate);
    }
    
    public void reportFailure() {
        Message.verbose("no failure report implemented by "+getName());
    }

    public void reportFailure(Artifact art) {
        Message.verbose("no failure report implemented by "+getName());
    }

    public OrganisationEntry[] listOrganisations() {
        return new OrganisationEntry[0];
    }
    public ModuleEntry[] listModules(OrganisationEntry org) {
        return new ModuleEntry[0];
    }
    public RevisionEntry[] listRevisions(ModuleEntry module) {
        return new RevisionEntry[0];
    }

    public String toString() {
        return getName();
    }
    public void dumpConfig() {
        Message.verbose("\t"+getName()+" ["+getTypeName()+"]");
    }

    public String getTypeName() {
        return getClass().getName();
    }
    /**
     * Default implementation actually download the artifact
     * Subclasses should overwrite this to avoid the download
     */
    public boolean exists(Artifact artifact) {
        DownloadReport dr = download(new Artifact[] {artifact}, getIvy(), getIvy().getDefaultCache());
        ArtifactDownloadReport adr = dr.getArtifactReport(artifact);
        return adr.getDownloadStatus() != DownloadStatus.FAILED;
    }

    public LatestStrategy getLatestStrategy() {        
        if (_latestStrategy == null) {
            if (getIvy() != null) {
                if (_latestStrategyName != null && !"default".equals(_latestStrategyName)) {
                    _latestStrategy = getIvy().getLatestStrategy(_latestStrategyName);
                    if (_latestStrategy == null) {
                        Message.error("unknown latest strategy: "+_latestStrategyName);
                        _latestStrategy = getIvy().getDefaultLatestStrategy();
                    }
                } else {
                    _latestStrategy = getIvy().getDefaultLatestStrategy();
                    Message.debug(getName()+": no latest strategy defined: using default");
                }
            } else {
                throw new IllegalStateException("no ivy instance found: impossible to get a latest strategy without ivy instance");
            }
        }
        return _latestStrategy;
    }
    

    public void setLatestStrategy(LatestStrategy latestStrategy) {
        _latestStrategy = latestStrategy;
    }    

    public void setLatest(String strategyName) {
        _latestStrategyName = strategyName;
    }    
    
    public String getLatest() {
        if (_latestStrategyName == null) {
            _latestStrategyName = "default";
        }
        return _latestStrategyName;
    }

    public Namespace getNamespace() {        
        if (_namespace == null) {
            if (getIvy() != null) {
                if (_namespaceName != null) {
                    _namespace = getIvy().getNamespace(_namespaceName);
                    if (_namespace == null) {
                        Message.error("unknown namespace: "+_namespaceName);
                        _namespace = getIvy().getSystemNamespace();
                    }
                } else {
                    _namespace = getIvy().getSystemNamespace();
                    Message.debug(getName()+": no namespace defined: using system");
                }
            } else {
                Message.verbose(getName()+": no namespace defined nor ivy instance: using system namespace");
                _namespace = Namespace.SYSTEM_NAMESPACE;
            }
        }
        return _namespace;
    }
    
    public void setNamespace(String namespaceName) {
        _namespaceName = namespaceName;
    }    

}
