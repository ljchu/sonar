/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.api.rule;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @since 3.6
 */
public final class Severity {

  public static final String INFO = "INFO";
  public static final String MINOR = "MINOR";
  public static final String MAJOR = "MAJOR";
  public static final String CRITICAL = "CRITICAL";
  public static final String BLOCKER = "BLOCKER";

  /**
   * All the supported severity values, ordered from {@link #INFO} to {@link #BLOCKER}.
   */
  public static final List<String> ALL = ImmutableList.of(INFO, MINOR, MAJOR, CRITICAL, BLOCKER);

  public static String get(int ordinal) {
    return ALL.get(ordinal);
  }

  public static Integer ordinal(String severiy) {
    return ALL.indexOf(severiy);
  }

  private Severity() {
    // utility
  }

}
