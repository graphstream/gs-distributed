package org.graphstream.distributed.io.old;




public class DistributedGraphActions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// init
		String[] params = new String[args.length-1] ;
		for(int i = 1 ; i < args.length ; i++) {
			params[i-1] = args[i] ;
		}

		if(args[0].equalsIgnoreCase("tag_dgs")) {
			tag_dgs(params);
		}
		else if(args[0].equalsIgnoreCase("dispatch")) {
			dispatch(params);
		}
		else if(args[0].equalsIgnoreCase("run")) {
			//run(params);
		}
		else {
			System.out.println("Usage :");
			System.out.println("\t tag_dgs ");
			System.out.println("\t dispatch ");
			System.out.println("\t run ");
			System.out.println("\t test ");
		}

	}

	// tag_dgs
	public static void tag_dgs(String[] params) {
		if(params.length >= 3) {
			System.out.println("1/ Fichier source " + params[0] + " en cours de traitement") ;
			//GraphConvertDistributedPolicy1DataDist tagger = new GraphConvertDistributedPolicy1DataDist() ;
			//tagger.run(params);
			System.out.println("2/ Fichier source traite - generation des fichiers " + params[1]) ;
		}
		else {
			System.out.println("Usage : tag_dgs <sourceFile> <destinationFileId> <tag1> <tag2> ... <tagn>");
			System.exit(0);
		}
	}

	// dispatch
	public static void dispatch(String[] args) {
		if(args.length > 3) {
			DistributedGraphFileDispatch dispatcher = new DistributedGraphFileDispatch();
			dispatcher.run(args);
		}
		else {
			System.out.println("Usage : dispatch <infos.ftp> <fileName1.dgs> <instance1> ... <fileNameX> <instanceX>");
			System.exit(0);
		}
	}

	// run
	public static void run(String[] args) {
		if(args.length > 2) {
			DistributedGraphRunDgsMessage runner = new DistributedGraphRunDgsMessage() ;
			long t1 = System.currentTimeMillis() ;
			System.out.println("delta begin");
			//runner.run(args) ;
			long t2 = System.currentTimeMillis() ;
			System.out.println("delta t = " + (int)((t2-t1)/1000));
		}
		else {
			System.out.println("Usage : run <indexFile> <tag1> <instanceRmi1> <dgsFile1> ... <tagX> <instanceRmiX> <dgsFileX>");
		}
	}




}
