/**
 * IBM Confidential
 * OCO Source Materials
 * (C) Copyright IBM Corp. 2010, 2015
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what has been deposited with the U.S. Copyright Office.
 */

package com.ibm.bi.dml.runtime.instructions.cp;

import com.ibm.bi.dml.parser.Expression.ValueType;
import com.ibm.bi.dml.runtime.DMLRuntimeException;
import com.ibm.bi.dml.runtime.DMLUnsupportedOperationException;

public class StringObject extends ScalarObject 
{
	private static final int MAX_STRING_SIZE = 1*1024*1024; //1MB
	
	
	@SuppressWarnings("unused")
	private static final String _COPYRIGHT = "Licensed Materials - Property of IBM\n(C) Copyright IBM Corp. 2010, 2015\n" +
                                             "US Government Users Restricted Rights - Use, duplication  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.";
	
	private String _value;

	public StringObject (String val){
		this(null,val);
	}

	public StringObject(String name, String val){
		super(name, ValueType.STRING);
		_value = val;
	}
	
	@Override
	public boolean getBooleanValue(){
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLongValue(){
		throw new UnsupportedOperationException();
	}

	@Override
	public double getDoubleValue(){
		throw new UnsupportedOperationException();
	}

	@Override
	public String getStringValue(){
		return _value;
	}

	@Override
	public Object getValue(){
		return _value;
	}
	
	public String toString() { 
		return getStringValue();
	}

	@Override
	public String getDebugName() {
		return _value;
	}

	/**
	 * 
	 * @param len
	 * @throws DMLUnsupportedOperationException
	 */
	public static void checkMaxStringLength( long len ) 
		throws DMLRuntimeException
	{
		if( len > MAX_STRING_SIZE )
		{
			throw new DMLRuntimeException(
					"Output string length exceeds maximum scalar string length " +
					"("+len+" > "+MAX_STRING_SIZE+").");
		}
	}
}