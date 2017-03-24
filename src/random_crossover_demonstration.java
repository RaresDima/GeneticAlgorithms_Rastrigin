import java.util.Random;

public class random_crossover_demonstration {

    public static void random_crossover_demo() {

        Random random_generator = new Random(System.currentTimeMillis());

        bitset parent_0 = new bitset(14, random_generator),
                parent_1 = new bitset(14, random_generator);

        bitset descendant = new bitset(parent_0.length);

        int gene_counter = 0,
                next_cutoff_size,
                next_parent = 0;


        System.out.println("\nINITIAL PHASE\n");

        System.out.print("\tPARENT 0:\t");
        parent_0.display();

        System.out.print("\n\tPARENT 1:\t");
        parent_1.display();

        System.out.print("\n\n\tDESCENDANT:\t");
        descendant.display();

        System.out.println();

        int step = 0;

        while(gene_counter <= parent_0.length-1){

            ++step;
            System.out.println("____________________________________________________");
            System.out.println("\nSTART PHASE " + step);

            next_cutoff_size = random_generator.nextInt(parent_0.length - gene_counter + 1);

            if(next_cutoff_size == 0)
                System.out.println("\n\tGENES FROM PARENT " + next_parent + " REJECTED");
            else
                System.out.println("\n\tUSING GENES " + gene_counter + " - " + (gene_counter + next_cutoff_size - 1) + " FROM PARENT " + next_parent);

            if (next_parent == 0) {
                for (int i = gene_counter; i < gene_counter + next_cutoff_size; ++i)
                    descendant.bits[i] = parent_0.bits[i];

                next_parent = 1;
            }
            else {
                for (int i = gene_counter; i < gene_counter + next_cutoff_size; ++i)
                    descendant.bits[i] = parent_1.bits[i];

                next_parent = 0;
            }

            gene_counter += next_cutoff_size;

            System.out.print("\n\tDESCENDANT:\t");

            for (int i = 0; i < gene_counter; ++i)
                System.out.print(descendant.bits[i]);

            System.out.println("\n");

            if(step==25) break;
        }

        System.out.println("____________________________________________________");
        System.out.println("\nGENE RECOMBINATION SUCCESSFUL\n");

        System.out.print("\tPARENT 0:\t");
        parent_0.display();

        System.out.print("\n\tPARENT 1:\t");
        parent_1.display();

        System.out.print("\n\n\tDESCENDANT:\t");
        descendant.display();


        System.out.println();
    }

    public static void main(String[] args) {

        random_crossover_demo();

    }

}
