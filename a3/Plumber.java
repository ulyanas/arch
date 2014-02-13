
public class Plumber {
	public static void main(String argv[]) {
		/****************************************************************************
		 * Here we instantiate four filters.
		 ****************************************************************************/

		SourceFilter sourceFilterA = new SourceFilter("./SubSetA.dat");
		SourceFilter sourceFilterB = new SourceFilter("./SubSetB.dat");
		MergeFilter mergeFilter = new MergeFilter();
		NormalSinkFilter sinkFilter = new NormalSinkFilter("./TotalSet.dat");

		/****************************************************************************
		 * Here we connect the filters starting with two sink filters (Filter A,
		 * B) which we connect to merge filter. Then we connect sink filter to
		 * the merge .
		 ****************************************************************************/

		mergeFilter.Connect(sourceFilterA, 0, 0);
		mergeFilter.Connect(sourceFilterB, 1, 0);
		sinkFilter.Connect(mergeFilter);

		/****************************************************************************
		 * Here we start the filters up. All-in-all,... its really kind of
		 * boring.
		 ****************************************************************************/

		sourceFilterA.start();
		sourceFilterB.start();
		mergeFilter.start();
		sinkFilter.start();

		// NormalSinkFilter sinkFilterA = new
		// NormalSinkFilter("./SubSetAReadable.dat");
		// NormalSinkFilter sinkFilterB = new
		// NormalSinkFilter("./SubSetBReadable.dat");
		// sinkFilterA.Connect(sourceFilterA);
		// sinkFilterB.Connect(sourceFilterB);
		// sinkFilterA.start();
		// sinkFilterB.start();

	} // main
}
