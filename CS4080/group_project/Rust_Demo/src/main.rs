extern crate rand;
extern crate darwin_rs;
extern crate num_cpus;

use std::sync::Arc;
use rand::Rng;

// Internal modules
use darwin_rs::{Individual, SimulationBuilder, Population, PopulationBuilder};

fn calc_distance(cities: &[(f64, f64)], index1: usize, index2: usize) -> f64 {
    let (x1, y1) = cities[index1];
    let (x2, y2) = cities[index2];
    let x = x2 - x1;
    let y = y2 - y1;

    x.hypot(y)
}

fn generate_init_population(count: u32, cities: &Vec<(f64, f64)>) -> Vec<CityItem> {
    let mut result = Vec::new();

    let city = Arc::new(cities.clone());

    let mut path: Vec<usize> = (0..cities.len())
                                .map(|x| x as usize)
                                .collect();

    path.push(0);

    for _ in 0..count {
        result.push( CityItem { path: path.clone(), city: city.clone()});
    }

    result
}

fn make_all_populations(population_size: u32, generations: u32, cities: &Vec<(f64, f64)>) -> Vec<Population<CityItem>> {
    let mut result = Vec::new();

    let initial_population = generate_init_population(population_size, &cities);

    for i in 1..(generations + 1) {
        let pop = PopulationBuilder::<CityItem>::new()
            .set_id(i)
            .initial_population(&initial_population)
            .mutation_rate((1..10).cycle().take(population_size as usize).collect())
            .reset_limit_increment(100 * i)
            .reset_limit_start(100 * i)
            .reset_limit_end(1000 * i)
            .finalize().unwrap();

        result.push(pop)
    }

    result
}

#[derive(Debug, Clone)]
struct CityItem {
    path: Vec<usize>,
    city: Arc<Vec<(f64, f64)>>
}

impl Individual for CityItem {
    fn mutate(&mut self) {
        let mut rng = rand::thread_rng();
        let index1: usize = rng.gen_range(1, self.city.len());
        let mut index2: usize = rng.gen_range(1, self.city.len());

        while index1 == index2 {
            index2 = rng.gen_range(1, self.city.len());
        }


        let tmp = self.path.remove(index1);
        self.path.insert(index2, tmp);
    }

    fn calculate_fitness(&mut self) -> f64 {
        //let mut prev_index = &(self.city.len() - 1); this causes double counting
        let mut prev_index = &(0);
        let mut length: f64 = 0.0;

        for index in &self.path {
            length += calc_distance(&self.city, *prev_index, *index);

            prev_index = index;
        }
        length
    }

    fn reset(&mut self) {
        let mut path: Vec<usize> = (0..self.city.len()).map(|x| x as usize).collect();
        path.push(0);

        self.path = path;
    }
}

fn main() {
    //http://www.math.uwaterloo.ca/tsp/world/qa194.tsp
    let cities = vec![
        (266.388900,0.000000), (537.777800,105.000000), (1049.722200,129.722200), (876.388900,152.222200), (1097.222200,179.722200), (1017.222200,293.888900), (1093.333300,306.666700), (1147.222200,413.055600), (1164.166700,420.277800), (221.111100,434.722200), (1416.666700,457.500000), (1351.111100,481.944500), (1019.166700,499.444500), (270.277800,507.777800), (1005.833300,518.611100), (1300.555600,565.833300), (1313.888900,579.722200), (1311.944500,591.388900), (1313.888900,596.388900), (850.555600,686.944500), (0.000000,712.222200), (978.333300,747.500000), (815.833300,766.666700), (1048.333300,781.666700), (837.500000,819.166700), (824.444500,843.333300), (1454.444500,959.444500), (1190.000000,969.444500), (359.444500,1054.722200), (641.388900,1145.833300), (1519.444500,1283.333300), (597.222200,1296.388900), (1320.277800,1352.222200), (1353.055600,1359.722200), (875.277800,1369.166700), (1498.611100,1383.055600), (1059.444500,1418.055600), (1137.222200,1641.388900),
    ];

    println!("Running test on {} cities", cities.len());

    let tsp = SimulationBuilder::<CityItem>::new()
        // optimum length is 6659.91
        .fitness(6660.0)
        .threads(8/*num_cpus::get()/2*/)
        .add_multiple_populations(make_all_populations(10, 10, &cities))
        .finalize();

    match tsp {
        Err(e) => println!("unexpected error: {}", e),
        Ok(mut tsp_simulation) => {
            tsp_simulation.run();

            tsp_simulation.print_fitness();

            //println!("Path and coordinates: ");
            for index in &tsp_simulation.simulation_result.fittest[0].individual.path {
                let (x, y) = cities[*index];
                println!("{},{}", x, y);
            }

            println!("total run time: {} seconds", tsp_simulation.total_time_in_ms/1000.0);
            println!("total distance: {}", tsp_simulation.simulation_result.fittest[0].individual.calculate_fitness());

        }
    }
}
