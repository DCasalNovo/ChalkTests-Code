import { useEffect, useState } from "react";
import {
  Exercise,
  ExerciseComponent,
  ExerciseComponentProps,
  ExerciseContext,
  ExerciseType,
} from "../Exercise/Exercise";
import { FaArrowRightFromBracket } from "react-icons/fa6";
import { Icon } from "../../interactiveElements/Icon";
import { PiChatsBold } from "react-icons/pi";
import {
  CreateTestActionKind,
  useCreateTestContext,
} from "./CreateTestContext";
import {
  CheckboxIcon,
  CheckedListIcon,
  GraduateIcon,
  LinkIcon,
  LockIcon,
  SchoolIcon,
  TextIcon,
  WorldSearchIcon,
} from "../SVGImages/SVGImages";

interface DragDropShowExerciseProps {
  position: string;
  exercise: Exercise;
  selectedExercise: boolean;
  setSelectedExercise: (value: string) => void;
}

export function DragDropShowExercise({
  position,
  exercise,
  selectedExercise,
  setSelectedExercise,
}: DragDropShowExerciseProps) {
  const [typeLabel, setTypeLabel] = useState(<></>);
  const [visibility, setVisibility] = useState(<></>);
  const [preview, setPreview] = useState(<></>);
  const { testState, dispatch } = useCreateTestContext();

  const exerciseComponent: ExerciseComponentProps = {
    exercise: exercise,
    position: position,
    context: { context: ExerciseContext.PREVIEW },
  };

  useEffect(() => {
    switch (exercise.type) {
      case ExerciseType.MULTIPLE_CHOICE:
        setTypeLabel(
          <label className="caracteristics-exercise gray-icon">
            <CheckedListIcon size="size-4" />
            Escolha múltipla
          </label>
        );

        setPreview(<ExerciseComponent {...exerciseComponent} />);
        break;
      case ExerciseType.OPEN_ANSWER:
        setTypeLabel(
          <label className="caracteristics-exercise gray-icon">
            <TextIcon size="size-4" />
            Resposta aberta
          </label>
        );
        setPreview(<ExerciseComponent {...exerciseComponent} />);
        break;
      case ExerciseType.TRUE_OR_FALSE:
        setTypeLabel(
          <label className="caracteristics-exercise gray-icon">
            <CheckboxIcon size="size-4" />
            Verdadeiro ou falso
          </label>
        );
        setPreview(<ExerciseComponent {...exerciseComponent} />);
        break;

      case ExerciseType.CHAT:
        setTypeLabel(
          <label className="caracteristics-exercise gray-icon">
            <div className="h-full scale-125">
              <PiChatsBold />
            </div>
            Chat Question
          </label>
        );
        setPreview(<ExerciseComponent {...exerciseComponent} />);
        break;
    }
  }, [exercise]);

  useEffect(() => {
    switch (exercise.identity.visibility) {
      case "private":
        setVisibility(
          <label className="caracteristics-exercise gray-icon">
            <LockIcon size="size-4" />
            Privado
          </label>
        );
        break;
      case "not-listed":
        setVisibility(
          <label className="caracteristics-exercise gray-icon">
            <LinkIcon size="size-4" />
            Não listado
          </label>
        );
        break;
      case "course":
        setVisibility(
          <label className="caracteristics-exercise gray-icon">
            <GraduateIcon size="size-4" />
            Curso
          </label>
        );
        break;
      case "institutional":
        setVisibility(
          <label className="caracteristics-exercise gray-icon">
            <SchoolIcon size="size-4" />
            Institucional
          </label>
        );
        break;
      case "public":
        setVisibility(
          <label className="caracteristics-exercise gray-icon">
            <WorldSearchIcon size="size-4" />
            Público
          </label>
        );
        break;
      default:
        break;
    }
  }, [exercise]);

  return (
    <div
      className={`${
        selectedExercise ? "max-h-full" : "max-h-[78px]"
      } transition-[max-height] overflow-hidden duration-300 rounded-lg bg-3-2 group-hover`}
    >
      <div className="flex flex-col h-full px-5 py-2.5">
        <div className="flex items-center text-sm font-normal transition-all mb-4 group">
          <button
            className="flex flex-col gap-1.5 h-14 justify-center cursor-default"
            onClick={() =>
              selectedExercise
                ? setSelectedExercise("")
                : setSelectedExercise(exercise.identity.id)
            }
          >
            <label className="flex min-w-max font-medium text-xl">
              {exercise.base.title}
            </label>
          </button>
          <button
            className={`${
              selectedExercise
                ? "mr-[75px] pr-4 border-r-2"
                : "group-hover:mr-[75px] group-hover:pr-4 group-hover:border-r-2"
            } pl-4 w-full h-full flex relative justify-end items-center gap-4 z-10 duration-100 transition-[margin] cursor-default bg-3-2 border-gray-1`}
            onClick={() =>
              selectedExercise
                ? setSelectedExercise("")
                : setSelectedExercise(exercise.identity.id)
            }
          >
            <div className="flex flex-col justify-center">
              {visibility}
              {typeLabel}
            </div>
          </button>
          <div className="flex flex-row-reverse w-0 items-center gap-4 z-0">
            <button
              className="btn-options-exercise gray-icon"
              onClick={() => {
                dispatch({
                  type: CreateTestActionKind.ADD_EXERCISE,
                  exercise: {
                    groupPosition: testState.groupPosition,
                    exercisePosition: testState.exercisePosition,
                    exercise: exercise,
                  },
                });
              }}
            >
              <FaArrowRightFromBracket />
              Adicionar
            </button>
          </div>
        </div>
        <div
          className={`${
            !selectedExercise ? "hidden" : "flex"
          } flex-wrap w-full text-sm font-normal gap-2 mx-1 mb-4 pb-4 border-b-2 border-gray-1`}
        >
          <div className="bg-yellow-600 tag-exercise">Matemática</div>
          <div className="bg-blue-600 tag-exercise">4º ano</div>
          <div className="bg-green-600 tag-exercise">escolinha</div>
          <div className="bg-blue-600 tag-exercise">4º ano</div>
          <div className="bg-green-600 tag-exercise">escolinha</div>
          <div className="bg-green-600 tag-exercise">escolinha</div>
          <div className="bg-blue-600 tag-exercise">4º ano</div>
          <div className="bg-green-600 tag-exercise">escolinha</div>
          <div className="bg-green-600 tag-exercise">escolinha</div>
          <div className="bg-green-600 tag-exercise">escolinha</div>
          <div className="bg-blue-600 tag-exercise">4º ano</div>
        </div>
        <div
          className={`${
            !selectedExercise ? "scale-y-0" : ""
          } flex flex-col mx-4 mb-4 border rounded-lg ex-1 border-gray-1`}
        >
          {preview}
        </div>
      </div>
    </div>
  );
}
