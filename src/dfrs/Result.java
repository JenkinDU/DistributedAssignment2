package dfrs;


/**
* dfrs/Result.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from D:/workspace/CORBA_Assignment/src/dfrs.idl
* 2016年10月27日 星期四 下午09时44分05秒 EDT
*/

public final class Result implements org.omg.CORBA.portable.IDLEntity
{
  public boolean success = false;
  public String content = null;

  public Result ()
  {
  } // ctor

  public Result (boolean _success, String _content)
  {
    success = _success;
    content = _content;
  } // ctor

} // class Result