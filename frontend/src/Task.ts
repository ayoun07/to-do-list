export interface TaskList {
  id: number;
  name: string;
}

export interface Task {
  id?: number;
  title: string;
  completed: boolean;
  list?: TaskList; // Remplace category par list
}
