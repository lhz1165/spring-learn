package com.lhz.spring.event;

import java.util.Observable;
import java.util.Observer;

/**
 * @author lhzlhz
 * @create 2020/8/26
 */
public class ObserverDemo {
	public static void main(String[] args) {
		Observable observable = new EventObservable();
		observable.addObserver(new EventObserver());
		observable.notifyObservers("hello");

	}

	static class EventObservable extends Observable {
		@Override
		protected synchronized void setChanged() {
			super.setChanged();
		}

		public void notifyObservers(Object arg) {
			setChanged();
			super.notifyObservers(arg);
			clearChanged();
		}
	}


	static class EventObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			System.out.println("收到数据！！！！！"+ arg);
		}
	}

}
