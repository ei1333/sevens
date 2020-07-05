
import java.util.EventListener;
import java.util.EventObject;

public interface IValueModel
{
	public static class Event extends EventObject
	{
		private Object value;
		public Event(IValueModel model, Object value)
		{
			super(value);
			this.value = value;
		}
		public Object getValue()
		{
			return(value);
		}
	}

	public interface Listener extends EventListener
	{
		public void valueChanged(IValueModel.Event e);
	}

	public void setValue(Object obj);
	public Object getValue();

	public void addListener(IValueModel.Listener l);
	public void removeListener(IValueModel.Listener l);
}
