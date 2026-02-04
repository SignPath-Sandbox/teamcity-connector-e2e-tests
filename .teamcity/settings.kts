import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.buildSteps.signPathSubmitSigningRequest
import jetbrains.buildServer.configs.kotlin.ParameterDisplay

version = "2024.12"

project {
  buildType {
    name = "Build and Sign"
    id("build_and_sign")

    params {
      select("ConnectorUrl", "https://teamcity-connector-stable.customersimulation.int.signpath.io", label = "Connector URL",
        display = ParameterDisplay.PROMPT,
        options = listOf(
          "https://teamcity-connector-stable.customersimulation.int.signpath.io",
          "https://teamcity-connector-playground.customersimulation.int.signpath.io"
        ))
      text("SignPath.OrganizationId", "9ff791fc-c563-44e3-ab8c-86a33c910bbe", label = "SignPath Organization ID", display = ParameterDisplay.PROMPT)
      text("SignPath.ProjectSlug", "TeamCity_Connector_E2E_Tests", label = "SignPath Project Slug", display = ParameterDisplay.PROMPT)
      text("SignPath.SigningPolicySlug", "test-signing", label = "SignPath Signing Policy Slug", display = ParameterDisplay.PROMPT)
      text("SignPath.ArtifactConfigurationSlug", "initial", label = "SignPath Artifact Configuration Slug", display = ParameterDisplay.PROMPT)
      password("SignPath.ApiToken", "credentialsJSON:4ba5812a-ffd4-41a7-829c-15adcebda622")
    }

    vcs {
      root(DslContext.settingsRoot)
      cleanCheckout = true
    }

    val artifactPath = "BuildOutput.zip"

    // publish the signed artifact
    artifactRules = artifactPath

    steps {
      // build step
      script {
        name = "Create artifact"
        scriptContent = "./build.sh"
        formatStderrAsError = true
      }

      // sign step
      signPathSubmitSigningRequest {
        connectorUrl = "%ConnectorUrl%"
        organizationId = "%SignPath.OrganizationId%"
        apiToken = "%SignPath.ApiToken%"
        projectSlug = "%SignPath.ProjectSlug%"
        signingPolicySlug = "%SignPath.SigningPolicySlug%"
        artifactConfigurationSlug = "%SignPath.ArtifactConfigurationSlug%"
        inputArtifactPath = artifactPath
        outputArtifactPath = artifactPath
        waitForCompletion = true
      }
    }

    // max build duration 5 mins
    failureConditions {
      executionTimeoutMin = 5
    }
  }
}
