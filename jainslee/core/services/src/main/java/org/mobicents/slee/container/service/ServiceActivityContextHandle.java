/**
 * 
 */
package org.mobicents.slee.container.service;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;

/**
 * @author martins
 *
 */
public class ServiceActivityContextHandle implements ActivityContextHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ServiceActivityHandleImpl activityHandle;
		
	/**
	 * 
	 */
	public ServiceActivityContextHandle(ServiceActivityHandleImpl activityHandle) {
		this.activityHandle = activityHandle;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivity()
	 */
	public Object getActivityObject() {
		return new ServiceActivityImpl(activityHandle.getServiceID());		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle()
	 */
	public ServiceActivityHandleImpl getActivityHandle() {
		return activityHandle;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType()
	 */
	public ActivityType getActivityType() {
		return ActivityType.SERVICE;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() == this.getClass()) {
			final ServiceActivityContextHandle other = (ServiceActivityContextHandle) obj;
			return other.activityHandle.equals(this.activityHandle);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return activityHandle.hashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder ("ACH=").append(getActivityType()).append('>').append(activityHandle).toString(); 		
	}
}