package utilityclasses;

public enum BatteryConsumptionStatus {

	HIGH(1000), NORMAL(5000), LOW(10000);

	private int dischargeLevel;

	private BatteryConsumptionStatus(int i) {

	}

	public int getDischargeLevel() {
		return dischargeLevel;
	}

	public void setDischargeLevel(int dischargeLevel) {
		this.dischargeLevel = dischargeLevel;
	}

}
