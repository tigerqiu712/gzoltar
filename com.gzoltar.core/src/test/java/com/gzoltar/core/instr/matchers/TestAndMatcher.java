/**
 * Copyright (C) 2018 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.gzoltar.examples.PrivateModifiers;
import org.gzoltar.examples.ProtectedModifiers;
import org.gzoltar.examples.PublicFinalModifiers;
import org.gzoltar.examples.PublicModifiers;
import org.junit.Test;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.AccessFlag;

public class TestAndMatcher {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void testPublicAndFinalMethods() throws Exception {
    MethodModifierMatcher publicMethodsMatcher = new MethodModifierMatcher(AccessFlag.PUBLIC);
    MethodModifierMatcher protectedMethodsMatcher = new MethodModifierMatcher(AccessFlag.FINAL);
    AndMatcher orMatcher = new AndMatcher(publicMethodsMatcher, protectedMethodsMatcher);

    CtClass ctClass = pool.get(PublicFinalModifiers.class.getCanonicalName());
    CtBehavior ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertTrue(orMatcher.matches(ctBehavior));

    ctClass = pool.get(PublicModifiers.class.getCanonicalName());
    ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertFalse(orMatcher.matches(ctBehavior));

    ctClass = pool.get(ProtectedModifiers.class.getCanonicalName());
    ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertFalse(orMatcher.matches(ctBehavior));

    ctClass = pool.get(PrivateModifiers.class.getCanonicalName());
    ctBehavior = ctClass.getDeclaredMethod("isNegative");
    assertFalse(orMatcher.matches(ctBehavior));
  }

}
