/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.hql.internal.ast.tree;
import antlr.SemanticException;
import antlr.collections.AST;

/**
 * A select expression that was generated by a FROM element.
 *
 * @author josh
 */
public class SelectExpressionImpl extends FromReferenceNode implements SelectExpression {

	public void resolveIndex(AST parent) throws SemanticException {
		throw new UnsupportedOperationException();
	}

	public void setScalarColumnText(int i) throws SemanticException {
		String text = getFromElement().renderScalarIdentifierSelect( i );
		setText( text );
	}

	public void resolve(boolean generateJoin, boolean implicitJoin, String classAlias, AST parent, AST parentPredicate) throws SemanticException {
		// Generated select expressions are already resolved, nothing to do.
		return;
	}
}
