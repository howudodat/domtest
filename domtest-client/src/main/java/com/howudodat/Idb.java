package com.howudodat;

import com.google.gwt.core.client.GWT;

import elemental2.dom.DomGlobal;
import elemental2.dom.Window;
import elemental2.indexeddb.IDBDatabase;
import elemental2.indexeddb.IDBFactory;
import elemental2.indexeddb.IDBObjectStore;
import elemental2.indexeddb.IDBOpenDBRequest;
import elemental2.indexeddb.IDBRequest;
import elemental2.indexeddb.IDBTransaction;
import elemental2.indexeddb.IndexedDbGlobal;
import elemental2.promise.Promise;
import jsinterop.base.Js;

public class Idb {

	private static final String DBNAME = "howudodat";
	private static final double DBVERSION = 1.0;
	private static final String STORENAME = "data";

	public static Promise<IDBDatabase> Open() {
		Window window = DomGlobal.window;
		IDBFactory indexedDB = IndexedDbGlobal.indexedDB;

		if (Js.asPropertyMap(window).has("indexedDB")) {
		}
		if (indexedDB != null) {
		}

		return new Promise<IDBDatabase>((resolve, reject) -> {
			IDBOpenDBRequest openDBRequest = indexedDB.open(DBNAME, DBVERSION);

			openDBRequest.onerror = event -> {
				GWT.log("Error opening DB: " + ((IDBOpenDBRequest)event.target).error.toString());
				return null;
			};

			openDBRequest.onsuccess = event -> {
				IDBDatabase db = (IDBDatabase) openDBRequest.result;
				resolve.onInvoke(db);
				return null;
			};

			openDBRequest.onupgradeneeded = event -> {
				// Create an objectStore for this database
				IDBDatabase db = (IDBDatabase) openDBRequest.result;
				db.createObjectStore(STORENAME);
				return null;
			};
		});
	}

	public static void SetData(String key, String value) {
		Idb.Open().then( db-> {
			IDBTransaction transaction = db.transaction(STORENAME, "readwrite");
			IDBObjectStore store = transaction.objectStore(STORENAME);
			IDBRequest request = store.put(value, key);
			request.onsuccess = event -> {
				return null;
			};
			request.onerror = event -> {
				GWT.log("Error Storing");
				return null;
			};
			return null;
		});
	}

	public static Promise<Pair<String,String>> GetData(String key) {
		return new Promise<Pair<String,String>>((resolve, reject) -> {
			Idb.Open().then( db-> {
				IDBTransaction transaction = db.transaction(STORENAME, "readonly");
				IDBObjectStore store = transaction.objectStore(STORENAME);

				IDBRequest<?> request = store.get(key);
				request.onsuccess = event -> {

					IDBRequest req = (event.currentTarget == null) ? null : Js.cast(event.currentTarget);
					String result = (req.result == null) ? null : Js.cast(req.result);
					resolve.onInvoke(new Pair(key, result));
					return null;
				};

				return null;
			});
		});
	}

	public static Promise<?> DeleteKey(String key) {
		return new Promise<String>((resolve, reject) -> {
			Idb.Open().then(db-> {
				IDBTransaction transaction = db.transaction(STORENAME, "readwrite");
				IDBObjectStore store = transaction.objectStore(STORENAME);
				IDBRequest request = store.delete(key);
				request.onsuccess = event -> {
					resolve.onInvoke(key);
					return null;
				};
				request.onerror = event -> {
					return null;
				};
				return null;
			});
		});
	}
}
