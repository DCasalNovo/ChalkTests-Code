import { RubricEdit } from "./RubricEdit";
import { RubricPreview } from "./RubricPreview";

export enum StardardLevels {
  EXCELENT = "5",
  WELL_DONE = "4",
  SATISFACTORY = "3",
  INSATISFACTORY = "2",
}

export interface Standard {
  title: StardardLevels;
  description: string;
  percentage: number;
}

export interface Criteria {
  title: string;
  points: number;
  standards: Standard[];
}

export interface Rubric {
  criteria: Criteria[];
}

export enum RubricContext {
  EDIT = "EDIT",
  PREVIEW = "PREVIEW",
}

export interface RubricProps {
  context: RubricContext;
  rubric: Rubric;
}

function createStandards(): Standard[] {
  let standards: Standard[] = [];
  Object.keys(StardardLevels).map((title) => {
    standards.push({
      title: title as StardardLevels,
      description: "",
      percentage: 0,
    });
  });
  return standards;
}

export function createNewCriteria(): Criteria {
  return { title: "", points: 0, standards: createStandards() };
}

export function Rubric({ context, rubric }: RubricProps) {
  switch (context) {
    case RubricContext.PREVIEW:
      return RubricPreview(rubric);

    case RubricContext.EDIT:
      return RubricEdit(rubric);
  }
}
