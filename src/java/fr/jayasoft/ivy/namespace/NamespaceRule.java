/*
 * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.
 * Copyright Jayasoft 2005 - All rights reserved
 * 
 * #SNAPSHOT#
 */
package fr.jayasoft.ivy.namespace;

public class NamespaceRule {
    private MRIDTransformationRule _fromSystem;
    private MRIDTransformationRule _toSystem;
    public MRIDTransformationRule getFromSystem() {
        return _fromSystem;
    }
    public void addFromsystem(MRIDTransformationRule fromSystem) {
        if (_fromSystem != null) {
            throw new IllegalArgumentException("only one fromsystem is allowed per rule");
        }
        _fromSystem = fromSystem;
    }
    public MRIDTransformationRule getToSystem() {
        return _toSystem;
    }
    public void addTosystem(MRIDTransformationRule toSystem) {
        if (_toSystem != null) {
            throw new IllegalArgumentException("only one tosystem is allowed per rule");
        }
        _toSystem = toSystem;
    }
}
