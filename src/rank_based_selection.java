import java.util.Random;
import java.util.Vector;

@SuppressWarnings("ALL")

public class rank_based_selection {

    /**
     *   FITNESS FUNCTION PARAMETERS
     */

    final static double start     = -5.12,
                        stop      =  5.12;
    final static int    precision =  3;

    /**
     *   FITNESS FUNCTIONS
     */

    public static double compute_rastrigin_function(bitset x) {

        return 10 * 1 + (Math.pow(x.to_decimal(start, stop, precision), 2) - 10 * Math.cos(2 * Math.PI * x.to_decimal(start, stop, precision)));
    }
    public static double compute_fitness(bitset x)            {

        return ((-1) * compute_rastrigin_function(x) + 50.0);
    }

    /**
     *   GENETIC OPERATORS
     */

    public static bitset mutation(bitset parent)                                     {

        Random random_mutator = new Random(System.currentTimeMillis());

        bitset descendant = new bitset(parent);

        descendant.flip(random_mutator.nextInt(parent.length));

        return descendant;
    }
    public static Vector<bitset> random_crossover(bitset parent_1, bitset parent_2)  {

        Random random_cutoff_generator = new Random(System.currentTimeMillis());

        Vector<bitset> descendants = new Vector<bitset>();
        descendants.addElement(new bitset(parent_1.length));
        descendants.addElement(new bitset(parent_1.length));

        int gene_counter = 0,
                next_cutoff_size,
                next_parent = 1;

        while(gene_counter <= parent_1.length - 1){

            next_cutoff_size = random_cutoff_generator.nextInt(parent_1.length - gene_counter + 1);

            if (next_parent == 0) {
                for (int i = gene_counter; i < gene_counter + next_cutoff_size; ++i) {
                    descendants.elementAt(0).bits[i] = parent_1.bits[i];
                    descendants.elementAt(1).bits[i] = parent_2.bits[i];
                }

                next_parent = 1;
            }
            else {
                for (int i = gene_counter; i < gene_counter + next_cutoff_size; ++i) {
                    descendants.elementAt(1).bits[i] = parent_1.bits[i];
                    descendants.elementAt(0).bits[i] = parent_2.bits[i];
                }

                next_parent = 0;
            }

            gene_counter += next_cutoff_size;
        }

        return descendants;
    }
    public static Vector<bitset> uniform_crossover(bitset parent_1, bitset parent_2) {

        Random next_parent_selector = new Random(System.currentTimeMillis());

        Vector<bitset> descendants = new Vector<bitset>();

        descendants.addElement(new bitset(parent_1.length));
        descendants.addElement(new bitset(parent_1.length));

        for (int i = 0; i < parent_1.length; ++i) {
            if (next_parent_selector.nextInt(2) == 0) {
                descendants.elementAt(0).bits[i] = parent_1.bits[i];
                descendants.elementAt(1).bits[i] = parent_2.bits[i];
            }
            else {
                descendants.elementAt(1).bits[i] = parent_1.bits[i];
                descendants.elementAt(0).bits[i] = parent_2.bits[i];
            }
        }

        return descendants;
    }

    /**
     *   UTILITY FUNCTIONS
     */

    public static int select_best_individual(Vector<bitset> population) {

        int    best_index   = 0;
        double best_fitness = compute_fitness(population.elementAt(0));

        for(int i = 1; i < population.size(); ++i)
            if (compute_fitness(population.elementAt(i)) > best_fitness) {
                best_fitness = compute_fitness(population.elementAt(i));
                best_index = i;
            }

        return best_index;
    }

    public static void main(String[] args){

        /**
         *   VARIABLE DECLARATION & INITIALIZATION
         */

        Random random_generator = new Random(System.currentTimeMillis());

        //  GENERERATE P(t) - INITIAL
        Vector<bitset> population = new Vector<>();
        for(int i = 0; i < 30; ++i)
            population.addElement(new bitset(14, random_generator));

        //  EVALUATE P(t)   - INITIAL
        Vector<Double> population_eval = new Vector<>();
        for (int i = 0; i < 30; ++i)
            population_eval.addElement(compute_fitness(population.elementAt(i)));

        // ELITISM STORAGE
        bitset elite_1 = new bitset(14),
                elite_2 = new bitset(14),
                elite_3 = new bitset(14);

        //  ROULETTE WHEEL VARIABLES
        int    recombination_method;
        double total_fitness,
                next_selected_individual;
        Vector<Double> individual_selection_probability = new Vector<>();
        Vector<Double> cumulative_selection_probability = new Vector<>();
        Vector<bitset> selected_population              = new Vector<>();

        System.out.println("\nINITIALIZATION SUCCESSFUL");

        System.out.print("\n\nGENERATION   0:\t0.\t");

        for (int i = 0; i < population.elementAt(0).length; ++i)
            System.out.print(population.elementAt(0).bits[i] + " ");

        System.out.print("\t");
        if (population.elementAt(0).to_decimal(start, stop, precision) >= 0.0)
            System.out.print(" ");
        System.out.printf("%.15f", population.elementAt(0).to_decimal(start, stop, precision));

        System.out.print("\t\t");
        if (population.elementAt(0).to_decimal(start, stop, precision) < 10.0)
            System.out.print(" ");
        System.out.printf("%.15f\n", population_eval.elementAt(0));

        for (int i = 1; i < population.size(); ++i){
            System.out.print("\t\t\t\t" + i +".\t");
            for (int j = 0; j < population.elementAt(i).length; ++j)
                System.out.print(population.elementAt(i).bits[j] + " ");

            System.out.print("\t");
            if (population.elementAt(i).to_decimal(start, stop, precision) >= 0.0)
                System.out.print(" ");
            System.out.printf("%.15f", population.elementAt(i).to_decimal(start, stop, precision));

            System.out.print("\t\t");
            if (population.elementAt(i).to_decimal(start, stop, precision) < 10.0)
                System.out.print(" ");
            System.out.printf("%.15f\n", population_eval.elementAt(i));
        }

        System.out.print("\nFITEST INDIVIDUAL :\t");
        for (int i = 0; i < population.elementAt(select_best_individual(population)).length; ++i)
            System.out.print(population.elementAt(select_best_individual(population)).bits[i] + " ");

        System.out.print("\t");
        if (population.elementAt(select_best_individual(population)).to_decimal(start, stop, precision) >= 0.0)
            System.out.print(" ");
        System.out.printf("%.15f", population.elementAt(select_best_individual(population)).to_decimal(start, stop, precision));

        System.out.print("\t\t");
        if (population.elementAt(select_best_individual(population)).to_decimal(start, stop, precision) < 10.0)
            System.out.print(" ");
        System.out.printf("%.15f\n", population_eval.elementAt(select_best_individual(population)));

        System.out.println();

        /**
         *   GENERATION SELECTION & RECOMBINATION
         *   * RANK BASED
         *   * ELITISM = 3
         */

        //  100 GENERATIONS, STARTING AT t = 0
        for (int generation = 0; generation < 100; ++generation){

            //  BEST 3 INDIVIDUALS ARE SAVED
            elite_1.copy_from(population.elementAt(select_best_individual(population)));
            population_eval.remove(select_best_individual(population));
            population.remove(select_best_individual(population));

            elite_2.copy_from(population.elementAt(select_best_individual(population)));
            population_eval.remove(select_best_individual(population));
            population.remove(select_best_individual(population));

            elite_3.copy_from(population.elementAt(select_best_individual(population)));
            population_eval.remove(select_best_individual(population));
            population.remove(select_best_individual(population));

            //  RANK BASED SELECTION, ELITISM AND RECOMBINATION:

            //    1. FITNESS EVALUATION
            for (int i = 0; i < population.size(); ++i)
                population_eval.set(i, compute_fitness(population.elementAt(i)));

            //    1.1 ORDER BY RANK (a simple bubble sort)
            for (int i = 0; i < population.size() - 1; ++i)
                for (int j = i; j < population.size(); ++j)
                    if (population_eval.elementAt(i) > population_eval.elementAt(j)) {

                        population_eval.set(j, population_eval.set(i, population_eval.elementAt(j)));
                        population.set(j, population.set(i, population.elementAt(j)));

                        i = 0;
                    }

            //    1.2 SET FITNESS ACCORDING TO RANK
            for (int i = 0; i < population_eval.size(); ++i)
                population_eval.set(i, (double)i);

            //    2. TOTAL FITNESS
            total_fitness = 0.0;
            for (int i = 0; i < population.size(); ++i)
                total_fitness += population_eval.elementAt(i);

            //    3. INDIVIDUAL SELECTION PROBABILITY
            individual_selection_probability.clear();
            for (int i = 0; i < population.size(); ++i)
                individual_selection_probability.addElement(population_eval.elementAt(i)/total_fitness);

            //    4. CUMULATIVE SELECTION PROBABILITY
            cumulative_selection_probability.clear();
            cumulative_selection_probability.addElement(0.0);
            for (int i = 0; i < population.size(); ++i)
                cumulative_selection_probability.addElement(cumulative_selection_probability.elementAt(i) + individual_selection_probability.elementAt(i));

            //    5. SELECTION
            selected_population.clear();
            for (int i = 0; i < 17; ++i) {

                //  GENERATE r
                next_selected_individual = random_generator.nextDouble();
                if (next_selected_individual == 0)
                    next_selected_individual += 0.000000000001;

                // SELECT THE CORRESPONDING INDIVIDUAL
                for (int j = 0; j < cumulative_selection_probability.size() - 1; ++j) {

                    if (cumulative_selection_probability.elementAt(j) < next_selected_individual &&
                            next_selected_individual <= cumulative_selection_probability.elementAt(j + 1)) {

                        if (selected_population.contains(population.elementAt(j))) {
                            --i;
                        }
                        else {
                            selected_population.addElement(population.elementAt(j));
                            break;
                        }
                    }
                }
            }

            //    6. RECOMBINATION
            population.clear();
            for (int i = 0; i < selected_population.size(); ++i)
                population.addElement(selected_population.elementAt(i));
            selected_population.clear();

            while (selected_population.size() < 17){

                recombination_method = random_generator.nextInt(3);
                if (population.size() == 1)
                    recombination_method = 0;

                int parent_1_index, parent_2_index;

                switch (recombination_method){

                    case 0:
                        //  MUTATE BEST INDIVIDUAL
                        parent_1_index = select_best_individual(population);

                        selected_population.addElement(mutation(population.elementAt(parent_1_index)));

                        population.remove(parent_1_index);
                        break;

                    case 1:
                        //  UNIFORM CROSSOVER: BEST INDIVIDUAL + RANDOM MATE
                        parent_1_index = select_best_individual(population);
                        parent_2_index = random_generator.nextInt(population.size());

                        selected_population.addAll(uniform_crossover(population.elementAt(parent_1_index),
                                population.elementAt(parent_2_index)));

                        bitset remove_reference_1 = new bitset(population.elementAt(parent_2_index));
                        population.remove(parent_1_index);
                        population.remove(remove_reference_1);
                        break;

                    case 2:
                        //  RANDOM CROSSOVER: BEST INDIVIDUAL + RANDOM MATE
                        parent_1_index = select_best_individual(population);
                        parent_2_index = random_generator.nextInt(population.size());

                        selected_population.addAll(random_crossover(population.elementAt(parent_1_index),
                                population.elementAt(parent_2_index)));

                        bitset remove_reference_2 = new bitset(population.elementAt(parent_2_index));
                        population.remove(parent_1_index);
                        population.remove(remove_reference_2);
                        break;

                    default:
                        break;
                }
            }
            while (selected_population.size() > 17)
                selected_population.remove(selected_population.size() - 1);

            //    7. ELITISM
            selected_population.addElement(elite_1);
            selected_population.addElement(elite_2);
            selected_population.addElement(elite_3);

            //    7.5.  RANDOM NEW INDIVIDUALS
            for (int i = 0; i < 10; ++i)
                selected_population.addElement(new bitset(selected_population.elementAt(0).length, random_generator));

            //    8. POPULATION VECTOR ACTUATION
            population.clear();
            population_eval.clear();
            for (int i = 0; i < selected_population.size(); ++i){
                population.addElement(selected_population.elementAt(i));
                population_eval.addElement(compute_fitness(population.elementAt(i)));
            }
            selected_population.clear();
        }

        /**
         *   RESULT DISPLAY
         */

        System.out.print("\n\nGENERATION 100:\t0.\t");

        for (int i = 0; i < population.elementAt(0).length; ++i)
            System.out.print(population.elementAt(0).bits[i] + " ");

        System.out.print("\t");
        if (population.elementAt(0).to_decimal(start, stop, precision) >= 0.0)
            System.out.print(" ");
        System.out.printf("%.15f", population.elementAt(0).to_decimal(start, stop, precision));

        System.out.print("\t\t");
        if (population.elementAt(0).to_decimal(start, stop, precision) < 10.0)
            System.out.print(" ");
        System.out.printf("%.15f\n", population_eval.elementAt(0));

        for (int i = 1; i < population.size(); ++i){
            System.out.print("\t\t\t\t" + i +".\t");
            for (int j = 0; j < population.elementAt(i).length; ++j)
                System.out.print(population.elementAt(i).bits[j] + " ");

            System.out.print("\t");
            if (population.elementAt(i).to_decimal(start, stop, precision) >= 0.0)
                System.out.print(" ");
            System.out.printf("%.15f", population.elementAt(i).to_decimal(start, stop, precision));

            System.out.print("\t\t");
            if (population.elementAt(i).to_decimal(start, stop, precision) < 10.0)
                System.out.print(" ");
            System.out.printf("%.15f\n", population_eval.elementAt(i));
        }

        System.out.print("\nFITEST INDIVIDUAL :\t");
        for (int i = 0; i < population.elementAt(select_best_individual(population)).length; ++i)
            System.out.print(population.elementAt(select_best_individual(population)).bits[i] + " ");

        System.out.print("\t");
        if (population.elementAt(select_best_individual(population)).to_decimal(start, stop, precision) >= 0.0)
            System.out.print(" ");
        System.out.printf("%.15f", population.elementAt(select_best_individual(population)).to_decimal(start, stop, precision));

        System.out.print("\t\t");
        if (population.elementAt(select_best_individual(population)).to_decimal(start, stop, precision) < 10.0)
            System.out.print(" ");
        System.out.printf("%.15f\n", population_eval.elementAt(select_best_individual(population)));

        System.out.println();
    }
}

