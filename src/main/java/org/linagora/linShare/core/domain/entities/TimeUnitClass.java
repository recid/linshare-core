package org.linagora.linShare.core.domain.entities;

import org.linagora.linShare.core.domain.constants.TimeUnit;
import org.linagora.linShare.core.domain.constants.UnitType;

public class TimeUnitClass extends Unit<TimeUnit> {

		
	public TimeUnitClass() {
		super();
	}
	
	@Override
	public UnitType getUnitType() {
		return UnitType.TIME;
	}

	@Override
	public String toString() {
		return getUnitType().toString() + ":" + unitValue.toString();
	}

	public TimeUnitClass(TimeUnit unit_value) {
		super(unit_value);
	}
	
	public int toCalendarValue() {
		return unitValue.toCalendarValue();
	}
}