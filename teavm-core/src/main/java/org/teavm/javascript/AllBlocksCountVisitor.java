/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.javascript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.teavm.javascript.ast.*;

/**
 *
 * @author Alexey Andreev
 */
class AllBlocksCountVisitor implements StatementVisitor {
    private Map<IdentifiedStatement, Integer> blocksCount = new HashMap<>();
    private IdentifiedStatement currentBlock;

    public void visit(List<Statement> statements) {
        if (statements == null) {
            return;
        }
        for (Statement part : statements) {
            part.acceptVisitor(this);
        }
    }

    public int getCount(IdentifiedStatement statement) {
        Integer result = blocksCount.get(statement);
        return result != null ? result : 0;
    }

    @Override
    public void visit(AssignmentStatement statement) {
    }

    @Override
    public void visit(SequentialStatement statement) {
        visit(statement.getSequence());
    }

    @Override
    public void visit(ConditionalStatement statement) {
        visit(statement.getConsequent());
        visit(statement.getAlternative());
    }

    @Override
    public void visit(SwitchStatement statement) {
        IdentifiedStatement oldCurrentBlock = currentBlock;
        currentBlock = statement;
        for (SwitchClause clause : statement.getClauses()) {
            visit(clause.getBody());
        }
        visit(statement.getDefaultClause());
        currentBlock = oldCurrentBlock;
    }

    @Override
    public void visit(WhileStatement statement) {
        IdentifiedStatement oldCurrentBlock = currentBlock;
        currentBlock = statement;
        visit(statement.getBody());
        currentBlock = oldCurrentBlock;
    }

    @Override
    public void visit(BlockStatement statement) {
        IdentifiedStatement oldCurrentBlock = currentBlock;
        currentBlock = statement;
        visit(statement.getBody());
        currentBlock = oldCurrentBlock;
    }

    @Override
    public void visit(BreakStatement statement) {
        IdentifiedStatement target = statement.getTarget();
        if (target == null) {
            target = currentBlock;
        }
        blocksCount.put(target, getCount(target) + 1);
    }

    @Override
    public void visit(ContinueStatement statement) {
        IdentifiedStatement target = statement.getTarget();
        if (target == null) {
            target = currentBlock;
        }
        blocksCount.put(target, getCount(target) + 1);
    }

    @Override
    public void visit(ReturnStatement statement) {
    }

    @Override
    public void visit(ThrowStatement statement) {
    }

    @Override
    public void visit(InitClassStatement statement) {
    }

    @Override
    public void visit(TryCatchStatement statement) {
        visit(statement.getProtectedBody());
        visit(statement.getHandler());
    }

    @Override
    public void visit(GotoPartStatement statement) {
    }

    @Override
    public void visit(MonitorEnterStatement statement) {
    }

    @Override
    public void visit(MonitorExitStatement statement) {
    }
}
