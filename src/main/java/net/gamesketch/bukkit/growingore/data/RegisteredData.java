package net.gamesketch.bukkit.growingore.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.block.Block;

import net.gamesketch.bukkit.growingore.Core;
import net.gamesketch.bukkit.growingore.methods.RegisteredOre;

public class RegisteredData {
	static File file = new File("plugins/growingore/registered.data");
	static File folder = new File("plugins/growingore/");
	
	public static RegisteredOre StringToData(String data) {
		String[] split = data.split(":");
		int x, y, z, oreid, interval;
		long cobble, stone, ore;
		Block b;
		try {
			x = Integer.parseInt(split[1]);
			y = Integer.parseInt(split[2]);
			z = Integer.parseInt(split[3]);
		
			b = Core.server.getWorld(split[0]).getBlockAt(x,y,z);
			oreid = Integer.parseInt(split[4]);
			interval = Integer.parseInt(split[5]);
			
			cobble = Long.parseLong(split[6]);
			stone = Long.parseLong(split[7]);
			ore = Long.parseLong(split[8]);
		} catch (NumberFormatException e) { return null; }
		return new RegisteredOre(b,oreid,interval,cobble,stone,ore);
	}
	public static String DataToString(RegisteredOre ore) {
		//world:x:y:z:oreID:interval:cobbleTimeLeft:stoneTimeLeft:oreTimeLeft
		String a1 = ore.getBlock().getWorld().getName();
		String a2 = "" + ore.getBlock().getX();
		String a3 = "" + ore.getBlock().getY();
		String a4 = "" + ore.getBlock().getZ();
		String a6 = "" + ore.getOreId();
		String a7 = "" + ore.getMinutes();
		String a8 = "" + ore.getCobbleTimeLeft();
		String a9 = "" + ore.getStoneTimeLeft();
		String a10 = "" + ore.getOreTimeLeft();
		return a1 + ":" + a2 + ":" + a3 + ":" + a4 + ":" + a6 + ":" + a7 + ":" + a8 + ":" + a9 + ":" + a10;
	}
	public static void Load() {
		if (!file.exists()) {
			try {
				folder.mkdirs();
				file.createNewFile();
			} catch (IOException e) { }
			return;
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String s;
			while ((s = in.readLine()) != null) {
				if (StringToData(s) != null) {
					Core.REGISTEREDORES.add(StringToData(s));
				} else { System.out.println("[GrowingOre] Failed data in registered.data file"); }
			}
			in.close();
		} catch (IOException e) { return; }
	}
	public static void Save() {
		if (!file.exists()) {
			try {
				folder.mkdirs();
				file.createNewFile();
			} catch (IOException e) { System.out.println("[GrowingOre] Failed to save registered.data"); return; }
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (RegisteredOre ore : Core.REGISTEREDORES) {
				out.write(DataToString(ore));
				out.newLine();
			}
			out.close();
		} catch (IOException e) { System.out.println("[GrowingOre] Failed to save registered.data"); return; }
	}
	
	
	
}
