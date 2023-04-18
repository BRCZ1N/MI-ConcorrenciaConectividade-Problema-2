package utilityclasses;

public enum BatteryConsumptionStatus {

	HIGH(5), NORMAL(10), LOW(15);

	private int dischargeLevel;

	private BatteryConsumptionStatus(int dischargeLevel) {
		
		this.dischargeLevel = dischargeLevel;

	}

	public int getDischargeLevel() {
		return dischargeLevel;
	}

	public void setDischargeLevel(int dischargeLevel) {
		this.dischargeLevel = dischargeLevel;
	}

}
