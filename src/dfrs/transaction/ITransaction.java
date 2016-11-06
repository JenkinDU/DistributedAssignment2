package dfrs.transaction;

public interface ITransaction {
	void doCommit() throws TransactionException ;
	void backCommit();
}
