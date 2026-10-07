package main

import (
	"errors"
	"fmt"
	"os"
	"os/exec"
	"runtime"
	"time"
)

const reportPath = "app/build/reports/tests/testDebugUnitTest/index.html"

func main() {
	wrapper := "./gradlew"
	if runtime.GOOS == "windows" {
		wrapper = "gradlew.bat"
	}

	// Any extra arguments go straight to Gradle, for example:
	// go run checktests.go --tests "*CakeListViewModelTest*"
	args := append([]string{"testDebugUnitTest"}, os.Args[1:]...)

	cmd := exec.Command(wrapper, args...)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	fmt.Printf("Running: %s %v\n\n", wrapper, args)

	start := time.Now()
	err := cmd.Run()
	elapsed := time.Since(start).Round(time.Second)

	if err != nil {
		fmt.Printf("\nFAILED after %s\n", elapsed)
		fmt.Printf("Report: %s\n", reportPath)

		var exitErr *exec.ExitError
		if errors.As(err, &exitErr) {
			os.Exit(exitErr.ExitCode())
		}
		fmt.Printf("Could not run Gradle: %v\n", err)
		os.Exit(1)
	}

	fmt.Printf("\nPASSED in %s\n", elapsed)
}
