/**
 * IBM Confidential
 * OCO Source Materials
 * (C) Copyright IBM Corp. 2010, 2013
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what has been deposited with the U.S. Copyright Office.
 */

package com.ibm.bi.dml.runtime.instructions.CPInstructions;

import com.ibm.bi.dml.parser.Expression.DataType;
import com.ibm.bi.dml.parser.Expression.ValueType;
import com.ibm.bi.dml.runtime.DMLRuntimeException;
import com.ibm.bi.dml.runtime.DMLUnsupportedOperationException;
import com.ibm.bi.dml.runtime.controlprogram.ExecutionContext;
import com.ibm.bi.dml.runtime.functionobjects.MaxIndex;
import com.ibm.bi.dml.runtime.functionobjects.SwapIndex;
import com.ibm.bi.dml.runtime.instructions.Instruction;
import com.ibm.bi.dml.runtime.matrix.io.MatrixBlock;
import com.ibm.bi.dml.runtime.matrix.operators.Operator;
import com.ibm.bi.dml.runtime.matrix.operators.ReorgOperator;


public class ReorgCPInstruction extends UnaryCPInstruction
{
	@SuppressWarnings("unused")
	private static final String _COPYRIGHT = "Licensed Materials - Property of IBM\n(C) Copyright IBM Corp. 2010, 2013\n" +
                                             "US Government Users Restricted Rights - Use, duplication  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.";
	
	public ReorgCPInstruction(Operator op, CPOperand in, CPOperand out, String istr){
		super(op, in, out, istr);
		cptype = CPINSTRUCTION_TYPE.Reorg;
	}
	
	public static Instruction parseInstruction ( String str ) 
		throws DMLRuntimeException {
		CPOperand in = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
		CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
		String opcode = parseUnaryInstruction(str, in, out);
		
		if ( opcode.equalsIgnoreCase("r'") ) {
			return new ReorgCPInstruction(new ReorgOperator(SwapIndex.getSwapIndexFnObject()), in, out, str);
		} 
		
		else if ( opcode.equalsIgnoreCase("rdiagV2M") ) {
			return new ReorgCPInstruction(new ReorgOperator(MaxIndex.getMaxIndexFnObject()), in, out, str);
		} 
		
		else {
			throw new DMLRuntimeException("Unknown opcode while parsing a ReorgInstruction: " + str);
		}
	}
	
	@Override
	public void processInstruction(ExecutionContext ec)
			throws DMLUnsupportedOperationException, DMLRuntimeException {
		long begin, st, tread, tcompute, twrite, ttotal;
		
		begin = System.currentTimeMillis();
		MatrixBlock matBlock = (MatrixBlock) ec.getMatrixInput(input1.get_name());
		tread = System.currentTimeMillis() - begin;
		
		st = System.currentTimeMillis();
		ReorgOperator r_op = (ReorgOperator) optr;
		String output_name = output.get_name();
		
		MatrixBlock soresBlock = (MatrixBlock) (matBlock.reorgOperations (r_op, new MatrixBlock(), 0, 0, 0));
        
		tcompute = System.currentTimeMillis() - st;
		
		st = System.currentTimeMillis();
		matBlock = null;
		ec.releaseMatrixInput(input1.get_name());
		ec.setMatrixOutput(output_name, soresBlock);
		soresBlock = null;
		
		twrite = System.currentTimeMillis() - st;
		ttotal = System.currentTimeMillis()-begin;
		
		LOG.trace("CPInst " + this.toString() + "\t" + tread + "\t" + tcompute + "\t" + twrite + "\t" + ttotal);
		
	}
	
}