package see;

import cfg.ICFGBasicBlockNode;
import cfg.ICFGDecisionNode;
import cfg.ICFGNode;
import expression.Expression;
import expression.IExpression;
import expression.IIdentifier;
import expression.Type;
import mycfg.CFGDecisionNode;
import set.SETBasicBlockNode;
import set.SETDecisionNode;
import set.SETExpressionVisitor;
import set.SETNode;
import statement.IStatement;
import statement.Statement;

import java.util.List;

public class SEENew {
    //  ICFGNode or CFGNode or ICFG or CFG? SETNode or SET
    public SETNode singleStep(ICFGNode icfgNode, SETNode setNode) throws Exception {

        //  initializing setNode aisehi
        SETNode returnSETNode = setNode;

        //  if s is an instruction
        if(icfgNode instanceof ICFGBasicBlockNode){

            // Assumption that, each icfgnode has only 1 instruction
            List<IStatement> statements = ((ICFGBasicBlockNode) icfgNode).getStatements();
            // if s is an instruction, then s' is also an instruction
            SETBasicBlockNode setBasicBlockNode = (SETBasicBlockNode)setNode;

            if(statements.size() != 1){
                //throw some exception InatomicInstructionException
            }

            for(IStatement statement : statements){
                SETExpressionVisitor visitor = new SETExpressionVisitor(setNode,
                        statement.getLHS().getType());
                IExpression value = null;

                IIdentifier LHS = statement.getLHS();
                IExpression RHS = statement.getRHS();

                visitor.visit(RHS); //  what does visit do?
                value = visitor.getValue(); // e' is value.

                // Symbolic values mapping
                IIdentifier var = LHS;
                setBasicBlockNode.setValue(var, value);
            }

            returnSETNode = setBasicBlockNode;
        }

        if(icfgNode instanceof ICFGDecisionNode){

            // if s is a decision node, then s' is also a decision node
            SETDecisionNode setDecisionNode = (SETDecisionNode) setNode;

            SETExpressionVisitor visitor = new SETExpressionVisitor(setNode,
                    Type.BOOLEAN);

            if (((ICFGDecisionNode) icfgNode).getCondition() == null) {
                throw new Exception("Null Expression");
            } else {
                visitor.visit(((ICFGDecisionNode) icfgNode).getCondition());
                IExpression value = visitor.getValue();
                setDecisionNode.setCondition(value);
            }

            returnSETNode = setDecisionNode;
        }



        return returnSETNode;
    }
}
