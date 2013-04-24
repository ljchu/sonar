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

package org.sonar.core.issue;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.sonar.api.BatchComponent;
import org.sonar.api.ServerComponent;
import org.sonar.api.issue.IssueQuery;
import org.sonar.core.persistence.MyBatis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @since 3.6
 */
public class IssueDao implements BatchComponent, ServerComponent {

  private final MyBatis mybatis;

  public IssueDao(MyBatis mybatis) {
    this.mybatis = mybatis;
  }

  public void insert(IssueDto issueDto) {
    SqlSession session = mybatis.openSession();
    try {
      // TODO bulk insert
      session.insert("org.sonar.core.issue.IssueMapper.insert", issueDto);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public IssueDao update(Collection<IssueDto> issues) {
    SqlSession session = mybatis.openBatchSession();
    try {
      // TODO bulk update
      for (IssueDto issue : issues) {
        session.update("org.sonar.core.issue.IssueMapper.update", issue);
      }
      session.commit();
      return this;

    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public IssueDto selectById(long id) {
    SqlSession session = mybatis.openSession();
    try {
      return session.selectOne("org.sonar.core.issue.IssueMapper.selectById", id);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public IssueDto selectByKey(String key) {
    SqlSession session = mybatis.openSession();
    try {
      return session.selectOne("org.sonar.core.issue.IssueMapper.selectByKey", key);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<IssueDto> selectOpenIssues(Integer componentId) {
    SqlSession session = mybatis.openSession();
    try {
      return session.selectList("org.sonar.core.issue.IssueMapper.selectOpenIssues", componentId);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<IssueDto> select(IssueQuery query) {
    SqlSession session = mybatis.openSession();
    try {
      return select(query, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<IssueDto> select(IssueQuery query, SqlSession session) {
    // TODO support ordering

    return session.selectList("org.sonar.core.issue.IssueMapper.select", query, new RowBounds(query.offset(), query.limit()));
  }

  /**
   * The returned IssueDto list contains only the issue id and the resource id
   */
  public List<IssueDto> selectIssueIdsAndComponentsId(IssueQuery query, SqlSession session) {
    // TODO support ordering

    return session.selectList("org.sonar.core.issue.IssueMapper.selectIssueIdsAndComponentsId", query);
  }

  public Collection<IssueDto> selectByIds(Collection<Long> ids, SqlSession session) {
    List <List<Long>> idsPartition = Lists.partition(newArrayList(ids), 1000);
    Map<String, List <List<Long>>> params = ImmutableMap.of("ids", idsPartition);
    return session.selectList("org.sonar.core.issue.IssueMapper.selectByIds", params);
  }

}