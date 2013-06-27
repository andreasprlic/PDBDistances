package com.ulyssecarion.pdb.distances.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ulyssecarion.pdb.distances.DistanceDataTree;

public class DistanceDataTreeSerializer {
	private static final String OUTPUT_FILE = "serialized_ddtrees"
			+ File.separator;
	private static final String EXTENSION = ".ser";

	public static void serialize(DistanceDataTree dataTree, String name) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(OUTPUT_FILE + name + EXTENSION));
			out.writeObject(dataTree);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DistanceDataTree deserialize(String name) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					OUTPUT_FILE + name + EXTENSION));

			DistanceDataTree dataTree = (DistanceDataTree) in.readObject();

			in.close();
			return dataTree;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
