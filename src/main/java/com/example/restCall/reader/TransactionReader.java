package com.example.restCall.reader;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.transform.FieldSet;

import com.example.restCall.model.Transaction;

public class TransactionReader implements ItemStreamReader<Transaction> {

	private ItemStreamReader<FieldSet> fieldSetReader;
	private int recordCount = 0;
	private int expectedRecordCount = 0;

	
	private StepExecution stepExecution;
	
	public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) {
		super();
		this.fieldSetReader = fieldSetReader;
		System.out.println("public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) ");
	}

	

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		this.fieldSetReader.update(executionContext);

	}

	@Override
	public void close() throws ItemStreamException {
		this.fieldSetReader.close();

	}

	@Override
	public Transaction read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		FieldSet readVal = fieldSetReader.read();
		System.out.println("-reader trans reader from read() "+this.toString());

		return process(readVal);
	}

	private Transaction process(FieldSet fieldSet) {
		Transaction result = null;
		if (fieldSet != null) {
			if (fieldSet.getFieldCount() > 1) {
				result = new Transaction();
				result.setAccountNumber(fieldSet.readString(0));
				result.setTimestamp(fieldSet.readDate(1, "yyyy-MM-DD HH:mm:ss"));
				result.setAmount(fieldSet.readDouble(2));
				recordCount++;
			} else {
				expectedRecordCount = fieldSet.readInt(0);
				if(expectedRecordCount != this.recordCount) {
					this.stepExecution.setTerminateOnly();
					}
			}
		}
		return result;
	}

	public void setFieldSetReader(ItemStreamReader<FieldSet> fieldSetReader) {
		this.fieldSetReader = fieldSetReader;
	}
	
	@BeforeStep
	public void beforeStep(StepExecution execution) {
	  this.stepExecution = execution;
	}
	@AfterStep
	public ExitStatus afterStep(StepExecution execution) {
		System.out.println("-afterStep trans reader from listener "+this.toString());
		if (recordCount == expectedRecordCount) {
			return execution.getExitStatus();
		} else {
			return ExitStatus.STOPPED;
		}
	}

	
	
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.fieldSetReader.open(executionContext);
	}
}
