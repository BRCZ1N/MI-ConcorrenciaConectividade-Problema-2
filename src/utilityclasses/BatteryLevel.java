package utilityclasses;

public enum BatteryLevel {

	DEFAULT(100), MEDIUM(50), LOW(25);

	private int batteryLevel;

	private BatteryLevel(int i) {

	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

}
