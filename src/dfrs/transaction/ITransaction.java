package dfrs.transaction;

public interface ITransaction {
	void doCommit();
	void backCommit();
}
